package pl.tysia.maggstone.data.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.provider.MediaStore
import android.util.Base64
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_MIN
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager
import androidx.room.Room
import pl.tysia.maggstone.R
import pl.tysia.maggstone.app.MaggApp
import pl.tysia.maggstone.data.Database
import pl.tysia.maggstone.data.NetAddressManager
import pl.tysia.maggstone.data.NetworkChangeReceiver
import pl.tysia.maggstone.data.Result
import pl.tysia.maggstone.data.model.Error
import pl.tysia.maggstone.data.model.Ware
import pl.tysia.maggstone.data.source.LoginDataSource
import pl.tysia.maggstone.data.source.LoginRepository
import pl.tysia.maggstone.data.source.PictureDataSource
import pl.tysia.maggstone.getPhotoString
import pl.tysia.maggstone.resizeBitmap
import pl.tysia.maggstone.rotateBitmap
import pl.tysia.maggstone.ui.SendingStateActivity
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import javax.inject.Inject
import kotlin.collections.ArrayList

const val BATCH_SIZE = 50000
const val NOTIFICATION_ID = 101

class SendingService : Service(){
    private var binder = SendingBinder()
    private var queue: MutableLiveData<Queue<QueueItem>> = MutableLiveData(ConcurrentLinkedQueue<QueueItem>())

    private var thread : Thread? = null

    @Inject lateinit var dataSource: PictureDataSource
    @Inject lateinit var db : Database

    var isRunning = false

    private fun readQueue(){
        db.queueDAO().getAll().observeForever {
            queue.value!!.addAll(it)
            queue.notifyObserver()
        }
    }

    override fun onBind(p0: Intent?): IBinder? {
        return binder
    }

    inner class SendingBinder : Binder(){
        fun getService(): SendingService = this@SendingService
    }

    fun addToQueue(ware: Ware) : QueueItem{
        val item = QueueItem(ware.name, ware.index!!, ware.id!!, ware.photoPath!!)

        addToQueue(item)

        return item
    }

    fun addToQueue(item: QueueItem){
        queue.value!!.add(item)
        queue.notifyObserver()
    }

    private fun loadPhotoBatches(queueItem: QueueItem){
        val file = File(queueItem.photoPath)

        val uri = Uri.fromFile(file)
        val bitmap = if(Build.VERSION.SDK_INT < 28) {
            MediaStore.Images.Media.getBitmap(
                this.contentResolver,
                uri
            )
        } else {
            val source = ImageDecoder.createSource(this.contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        }

        val pictureString = getPhotoString(bitmap)
        val pictureBatches = pictureString.chunked(BATCH_SIZE)

        queueItem.picture = pictureBatches

    }

    fun getQueue() = queue

    override fun onCreate() {

        (application as MaggApp).appComponent.inject(this)

        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        NetworkChangeReceiver.internetConnection.observeForever {
            if (it) queue.notifyObserver()
        }

        queue.observeForever {
            if (it.isNotEmpty() && thread == null){
                thread = SendingThread()
                thread?.start()
            }
        }

        readQueue()

    }

    private fun startForeground() {
        val pendingIntent: PendingIntent =
            Intent(this, SendingStateActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(this, 0, notificationIntent, 0)
            }

        val channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel("my_service", "Magg Background Service")
            } else {
               ""
            }

        val notificationBuilder = NotificationCompat.Builder(this, channelId )
        val notification = notificationBuilder.setOngoing(true)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(getText(R.string.notification_title))
            .setPriority(PRIORITY_MIN)
            .setContentIntent(pendingIntent)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()

        startForeground(NOTIFICATION_ID, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String): String{
        val chan = NotificationChannel(channelId,
            channelName, NotificationManager.IMPORTANCE_NONE)
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        isRunning = true

        startForeground()

        db.queueDAO().getAll().observeForever {
            queue.value!!.addAll(it)
            queue.notifyObserver()
        }
        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job

        return START_STICKY
    }

    fun sendPictureStart(item : QueueItem) : Int{
        val result = dataSource.sendPictureStart(item.wareID, item.picture!![0])
        if (result is Result.Success){
            item.batchesSent++
            return result.data
        }
        else if (result is Result.Error) {
            val error = Error(Error.TYPE_PICTURE,
                item.wareID.toString(),
                "Błąd przesyłania obrazu dla towaru : ${item.index}.",
                result.exception.message.toString())

            error.addQueueItemExtra(item)
            val res = db.errorsDAO().insert(error)
            print(res)
        }

        return -1
    }

    fun sendPictureNext(item : QueueItem) : Boolean{
        val sent = item.batchesSent
        val result = dataSource.sendPictureNext(item.photoID, item.picture!![sent], sent)
        if (result is Result.Success) {
            item.batchesSent++
            return result.data
        }
        else if (result is Result.Error) {
            val error = Error(Error.TYPE_PICTURE,
                item.wareID.toString(),
                "Błąd przesyłania obrazu dla towaru : ${item.index}.",
                result.exception.message.toString())

            error.addQueueItemExtra(item)
            db.errorsDAO().insert(error)
        }

        return false
    }

    fun sendPictureFin(item : QueueItem) : Boolean{
        val result = dataSource.sendPictureFin(item.photoID)
        if (result is Result.Success) return result.data
        else if (result is Result.Error) {
            val error = Error(Error.TYPE_PICTURE,
                item.wareID.toString(),
                "Błąd przesyłania obrazu dla towaru : ${item.index}.",
                result.exception.message.toString())

            error.addQueueItemExtra(item)
            db.errorsDAO().insert(error)

        }

        return false
    }

    private fun deletePhoto(url : String){
        val file = File(url)

        if(file.exists())
            file.delete()
    }

    inner class SendingThread : Thread() {
        override fun run() {
            super.run()

            while(queue.value!!.isNotEmpty()){
                val item = queue.value!!.peek()!!

                loadPhotoBatches(item)

                try {
                    if (item.photoID < 0) {
                        item.photoID = sendPictureStart(item)
                    }

                    var sendingState = true
                    if (item.photoID >= 0 ) while (!item.allSent() && sendingState) {
                        sendingState = sendPictureNext(item)
                    }

                    if (item.allSent())
                        item.finished = sendPictureFin(item)

                    if(item.finished) {
                        deletePhoto(item.photoPath)
                    }

                    queue.value!!.remove()
                    queue.notifyObserver()
                    db.queueDAO().delete(item)

                }catch (e : IOException){
                    db.queueDAO().addAll(ArrayList(queue.value!!))
                }
            }

            thread = null
            isRunning = false
            stopForeground(true)
            stopSelf()
        }
    }


}

fun <T> MutableLiveData<T>.notifyObserver() {
    this.postValue(this.value)
}