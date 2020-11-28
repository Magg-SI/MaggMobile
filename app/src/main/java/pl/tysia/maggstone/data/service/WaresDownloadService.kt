package pl.tysia.maggstone.data.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.*
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import pl.tysia.maggstone.data.*
import pl.tysia.maggstone.data.source.LoginDataSource
import pl.tysia.maggstone.data.source.LoginRepository
import pl.tysia.maggstone.data.source.WareDataSource

private const val COUNTER_PREFERENCES = "pl.tysia.maggstone.wares_counter_preferences"
private const val COUNTER = "pl.tysia.maggstone.counter"

class WaresDownloadService : Service() {
    var progress: MutableLiveData<Int> = MutableLiveData()
    private var binder = SendingBinder()

    override fun onBind(p0: Intent?): IBinder? {
        return binder
    }

    inner class SendingBinder : Binder(){
        fun getService(): WaresDownloadService = this@WaresDownloadService
    }


    companion object{
        var isRunning = false
    }


    private var serviceLooper: Looper? = null
    private var serviceHandler: ServiceHandler? = null

    // Handler that receives messages from the thread
    private inner class ServiceHandler(looper: Looper) : Handler(looper) {

        override fun handleMessage(msg: Message) {
            try {
                val dataSource = WareDataSource()

                val counter= getSharedPreferences(COUNTER_PREFERENCES, Context.MODE_PRIVATE).getInt(COUNTER, 0)
                val token = LoginRepository(
                    LoginDataSource(),
                    this@WaresDownloadService
                ).user!!.token

                val result = dataSource.getWares(token, counter)
                if (result is Result.Success) {
                    val page = result.data

                    var maxCounter = 0

                    val db = Room.databaseBuilder(
                        this@WaresDownloadService,
                        Database::class.java, "pl.tysia.database"
                    ).build()

                    val dao = db.waresDao()

                    dao.deleteAll()

                    var pagesDownloaded = 0

                    for (i in 0..page.pageCount!!){
                        val waresPage = dataSource.getWaresPage(token, page.listID!!, i)

                        if (waresPage is Result.Success){
                            val wares = waresPage.data.list!!

                            wares.forEach {
                                maxCounter = maxCounter.coerceAtLeast(it.counter!!)
                            }

                            pagesDownloaded += wares.size

                            dao.insertAll(wares)
                            progress.postValue((pagesDownloaded * 100f/ page.pageCount!!).toInt())
                        }

                    }

                    progress.postValue(100)


                    getSharedPreferences(COUNTER_PREFERENCES, Context.MODE_PRIVATE)
                        .edit()
                        .putInt(COUNTER, maxCounter)
                        .apply()



                }

            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
            }

            stopSelf(msg.arg1)
        }
    }

    override fun onCreate() {
        HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND).apply {
            start()

            serviceLooper = looper
            serviceHandler = ServiceHandler(looper)
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        isRunning = true
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show()

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        serviceHandler?.obtainMessage()?.also { msg ->
            msg.arg1 = startId
            serviceHandler?.sendMessage(msg)
        }

        // If we get killed, after returning from here, restart
        return START_STICKY
    }

    override fun onDestroy() {
        isRunning = false
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show()
    }
}