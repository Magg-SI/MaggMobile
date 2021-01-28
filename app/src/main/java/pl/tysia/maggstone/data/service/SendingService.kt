package pl.tysia.maggstone.data.service

import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.provider.MediaStore
import android.util.Base64
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager
import androidx.room.Room
import pl.tysia.maggstone.data.Database
import pl.tysia.maggstone.data.NetAddressManager
import pl.tysia.maggstone.data.NetworkChangeReceiver
import pl.tysia.maggstone.data.Result
import pl.tysia.maggstone.data.model.Error
import pl.tysia.maggstone.data.model.Ware
import pl.tysia.maggstone.data.source.LoginDataSource
import pl.tysia.maggstone.data.source.LoginRepository
import pl.tysia.maggstone.data.source.PictureDataSource
import pl.tysia.maggstone.resizeBitmap
import pl.tysia.maggstone.rotateBitmap
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.collections.ArrayList

const val BATCH_SIZE = 5000

class SendingService : Service(){
    private var binder = SendingBinder()
    private var queue: MutableLiveData<Queue<QueueItem>> = MutableLiveData(ConcurrentLinkedQueue<QueueItem>())

    private var thread : Thread? = null

    private lateinit var dataSource: PictureDataSource
    private lateinit var db : Database

    var isRunning = false

    private fun saveQueue(){
        SavingThread().start()
    }

    private fun readQueue(){
        db.queueDAO().getAll().observeForever {
            addToQueue(it)
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
        queue.value!!.add(item)
        queue.notifyObserver()

        return item
    }

    fun addToQueue(item: QueueItem){
        queue.value!!.add(item)
        queue.notifyObserver()
    }

    fun addToQueue(items: List<QueueItem>){
        queue.value!!.addAll(items)
        queue.notifyObserver()
    }

    private fun getPictureSize() : Float{
        return PreferenceManager
            .getDefaultSharedPreferences(this)
            .getString("picture_size", "0.1")!!.toFloat()
    }

    private fun getPhotoString(bitmap : Bitmap) : String{
        val stream = ByteArrayOutputStream()
        val resized = resizeBitmap(bitmap, getPictureSize())
        resized.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val image = stream.toByteArray()
        return Base64.encodeToString(image, Base64.DEFAULT)
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
        db = Room.databaseBuilder(
            this@SendingService,
            Database::class.java, "pl.tysia.database"
        ).build()

        dataSource = PictureDataSource(this@SendingService, NetAddressManager(this@SendingService))

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

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        isRunning = true

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job

        return START_STICKY
    }

    fun sendPictureStart(item : QueueItem, token : String) : Int{
        val result = dataSource.sendPictureStart(item.wareID, item.picture!![0], token)
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

    fun sendPictureNext(item : QueueItem, token : String) : Boolean{
        val sent = item.batchesSent
        val result = dataSource.sendPictureNext(item.photoID, item.picture!![sent], sent, token)
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

    fun sendPictureFin(item : QueueItem, token : String) : Boolean{
        val result = dataSource.sendPictureFin(token, item.photoID)
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

                val token =  LoginRepository(LoginDataSource(NetAddressManager(this@SendingService)),this@SendingService).user!!.token
                try {
                    if (item.photoID < 0) {
                        item.photoID = sendPictureStart(item, token)
                    }

                    var sendingState = true
                    if (item.photoID >= 0 ) while (!item.allSent() && sendingState) {
                        sendingState = sendPictureNext(item, token)
                    }

                    if (item.allSent())
                        item.finished = sendPictureFin(item, token)

                    if(item.finished)
                        deletePhoto(item.photoPath)

                    queue.value!!.remove()
                    queue.notifyObserver()
                    db.queueDAO().delete(item)
                }catch (e : IOException){
                }
            }

            thread = null
            isRunning = false
            stopSelf()
        }
    }

    inner class SavingThread : Thread() {
        override fun run() {
            super.run()

            db.queueDAO().addAll(ArrayList(queue.value))

        }
    }

    override fun onDestroy() {
        saveQueue()
    }
}

fun <T> MutableLiveData<T>.notifyObserver() {
    this.postValue(this.value)
}