package pl.tysia.maggstone.data.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.*
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import pl.tysia.maggstone.app.MaggApp
import pl.tysia.maggstone.data.*
import pl.tysia.maggstone.data.model.Error
import pl.tysia.maggstone.data.source.LoginDataSource
import pl.tysia.maggstone.data.source.LoginRepository
import pl.tysia.maggstone.data.source.WareDataSource
import java.io.IOException
import javax.inject.Inject

private const val ERROR_SOURCE = "pl.tysia.maggstone.error_source_wares"

class WaresDownloadService : Service() {
    var progress: MutableLiveData<Int> = MutableLiveData()
    private var binder = SendingBinder()

    @Inject lateinit var db : Database;
    @Inject lateinit var dataSource : WareDataSource;

    override fun onBind(p0: Intent?): IBinder? {
        return binder
    }

    inner class SendingBinder : Binder(){
        fun getService(): WaresDownloadService = this@WaresDownloadService
    }


    companion object{
        var isRunning = false
    }


    fun synchronise(){
        progress.postValue(0)

        serviceHandler?.obtainMessage()?.also { msg ->
            msg.arg1 = -1
            serviceHandler?.sendMessage(msg)
        }
    }

    private var serviceLooper: Looper? = null
    private var serviceHandler: ServiceHandler? = null

    // Handler that receives messages from the thread
    private inner class ServiceHandler(looper: Looper) : Handler(looper) {

        override fun handleMessage(msg: Message) {
            val errorsDAO = db.errorsDAO()

            errorsDAO.clearSource(ERROR_SOURCE)

            try {
                val dao = db.waresDao()

                val maxCounter = dao.getMaxCounter()



                val result = dataSource.getWares(maxCounter)
                if (result is Result.Success) {
                    val page = result.data

                    var pagesDownloaded = 0

                    for (i in 0..page.pageCount!!){
                        val waresPage = dataSource.getWaresPage(page.listID!!, i)

                        if (waresPage is Result.Success){
                            val wares = waresPage.data.list!!

                            pagesDownloaded += wares.size

                            val inserted = dao.insertAll(wares)
                            wares.removeAll { inserted.contains(it.id!!.toLong()) }
                            dao.updateAll(wares)

                            progress.postValue(((pagesDownloaded * 100f)/ page.pageCount!!).toInt())
                        }else if (result is Result.Error){
                            errorsDAO.insert(Error(Error.TYPE_DOWNLOAD, ERROR_SOURCE,"Pobieranie towarów nie powiodło się", result.exception.message!!))
                            break
                        }

                    }

                    progress.postValue(100)

                }else if (result is Result.Error){
                    errorsDAO.insert(Error(Error.TYPE_DOWNLOAD, ERROR_SOURCE, "Pobieranie towarów nie powiodło się", result.exception.message!!))
                }

            } catch (e: IOException) {
                errorsDAO.insert(Error(Error.TYPE_DOWNLOAD, ERROR_SOURCE,"Pobieranie towarów nie powiodło się", "Brak połączenia z internetem."))
                Toast.makeText(this@WaresDownloadService, "Pobieranie towarów nie powiodło się", Toast.LENGTH_LONG).show()
            }finally {
                if (msg.arg1 != -1)
                    stopSelf(msg.arg1)
            }

        }
    }

    override fun onCreate() {
        (application as MaggApp).appComponent.inject(this)

        HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND).apply {
            start()

            serviceLooper = looper
            serviceHandler = ServiceHandler(looper)
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        isRunning = true

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
    }
}