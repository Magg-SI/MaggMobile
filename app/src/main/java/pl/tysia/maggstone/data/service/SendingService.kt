package pl.tysia.maggstone.data.service

import android.app.Service
import android.content.Intent
import android.os.*
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import pl.tysia.maggstone.data.model.Ware
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

class SendingService : Service(){
    private var binder = SendingBinder()
    private var queue: MutableLiveData<Queue<QueueItem>> = MutableLiveData()

    private var thread : Thread? = null

    companion object{
        var isRunning = false
    }

    override fun onBind(p0: Intent?): IBinder? {
        return binder
    }

    inner class SendingBinder : Binder(){
        fun getService(): SendingService = this@SendingService
    }

    fun addToQueue(ware: Ware){
        queue.value!!.add(QueueItem(ware = ware))
        queue.notifyObserver()
    }

    fun getQueue() = queue

    override fun onCreate() {
        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.

        queue.value = ConcurrentLinkedQueue<QueueItem>()

    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Toast.makeText(this, "sending service starting", Toast.LENGTH_SHORT).show()

        isRunning = true

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job


        queue.observeForever(androidx.lifecycle.Observer {
            if (it.isNotEmpty() && thread == null){
                thread = SendingThread()
                thread?.start()
            }
        })

        val ware: Ware? = intent.getSerializableExtra(Ware.WARE_EXTRA) as Ware?
        if (ware != null){
            queue.value!!.add(QueueItem(ware = ware))
            queue.notifyObserver()
        }


        return Service.START_STICKY
    }

    inner class SendingThread : Thread() {
        override fun run() {
            super.run()
//            while(queue.isNotEmpty()){
//                    val token =  LoginRepository(LoginDataSource(),this@SendingService).user!!.token
//                    PictureDataSource(this@SendingService).sendPicture(queue.peek()!!.ware, token)
//                    queue.remove()
//                }
                for (i in 0..100){
                    sleep(5000)
                    val percentSent = queue.value!!.peek()!!.percentSent
                    percentSent.postValue(percentSent.value?.plus(10))
                }

                queue.value!!.remove()

                thread = null


            }
    }

    override fun onDestroy() {
        Toast.makeText(this, "sending service done", Toast.LENGTH_SHORT).show()

        isRunning = false
    }
}

fun <T> MutableLiveData<T>.notifyObserver() {
    this.value = this.value
}