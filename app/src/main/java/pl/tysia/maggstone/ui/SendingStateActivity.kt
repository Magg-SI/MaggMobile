package pl.tysia.maggstone.ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_new_document.*
import kotlinx.android.synthetic.main.activity_sending_state.*
import pl.tysia.maggstone.R
import pl.tysia.maggstone.data.model.Ware
import pl.tysia.maggstone.data.service.QueueItem
import pl.tysia.maggstone.data.service.SendingService
import pl.tysia.maggstone.ui.presentation_logic.adapter.SendingCatalogAdapter
import java.util.*

class SendingStateActivity : AppCompatActivity() {
    private lateinit var adapter : SendingCatalogAdapter
    private lateinit var mService: SendingService
    private var mBound: Boolean = false

    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            val binder = service as SendingService.SendingBinder
            mService = binder.getService()
            mBound = true

            mService.getQueue().observe(this@SendingStateActivity, androidx.lifecycle.Observer {
                adapter.allItems.clear()
                adapter.addAll(it)
                adapter.filter()
                adapter.notifyDataSetChanged()
            })

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
        setContentView(R.layout.activity_sending_state)

        adapter = SendingCatalogAdapter(ArrayList())

        recycler.adapter = adapter

        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycler.layoutManager = linearLayoutManager
    }
}