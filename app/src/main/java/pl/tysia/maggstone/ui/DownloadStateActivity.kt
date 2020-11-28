package pl.tysia.maggstone.ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.view.View
import androidx.lifecycle.observe
import kotlinx.android.synthetic.main.activity_download_state.*
import pl.tysia.maggstone.R
import pl.tysia.maggstone.data.service.ContractorsDownloadService
import pl.tysia.maggstone.data.service.SendingService
import pl.tysia.maggstone.data.service.WaresDownloadService
import pl.tysia.maggstone.ui.document.BasicNewDocumentActivity

class DownloadStateActivity : AppCompatActivity() {
    private lateinit var contractorsService: ContractorsDownloadService
    private lateinit var waresService: WaresDownloadService
    private var mBound: Boolean = false

    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            if (service is ContractorsDownloadService.SendingBinder) {
                contractorsService = service.getService()

                contractorsService.progress.observe(this@DownloadStateActivity) {
                    contractors_progress.progress = it
                }
            }
            else if (service is WaresDownloadService.SendingBinder){
                waresService = service.getService()

                waresService.progress.observe(this@DownloadStateActivity) {
                    wares_progress.progress = it
                }
            }

            mBound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
        }
    }

    override fun onStart() {
        super.onStart()
        // Bind to LocalService
        Intent(this, SendingService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download_state)
    }
}