package pl.tysia.maggstone.ui.error

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import kotlinx.android.synthetic.main.basic_catalog_layout.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.tysia.maggstone.R
import pl.tysia.maggstone.data.Database
import pl.tysia.maggstone.data.model.Error
import pl.tysia.maggstone.data.model.Ware
import pl.tysia.maggstone.data.service.SendingService
import pl.tysia.maggstone.ui.RecyclerMarginDecorator
import pl.tysia.maggstone.ui.login.afterTextChanged
import pl.tysia.maggstone.ui.presentation_logic.adapter.BasicCatalogAdapter
import pl.tysia.maggstone.ui.presentation_logic.adapter.ErrorsAdapter
import pl.tysia.maggstone.ui.presentation_logic.filterer.StringFilter
import pl.tysia.maggstone.ui.showYesNoDialog
import java.util.ArrayList

class ErrorsActivity : AppCompatActivity(), ErrorsAdapter.ErrorsListener {
    private lateinit var adapter: ErrorsAdapter
    private lateinit var mService: SendingService
    private var mBound: Boolean = false

    private lateinit var db : Database

    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as SendingService.SendingBinder
            mService = binder.getService()
            mBound = true

        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_errors)

        db = Room.databaseBuilder(
            this@ErrorsActivity,
            Database::class.java, "pl.tysia.database"
        ).build()

        adapter = ErrorsAdapter(ArrayList())
        adapter.setListener(this)

        recyclerView.adapter = adapter

        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager

        recyclerView.addItemDecoration(RecyclerMarginDecorator(mTopFirst = resources.getDimension(R.dimen.top_label_size).toInt(), mBottomLast = 16))


    }

    override fun onResume() {
        super.onResume()
        db.errorsDAO().getAll().observe(this, Observer{ errors ->
            adapter.allItems.clear()
            adapter.addAll(errors)
            adapter.filter()
            adapter.notifyDataSetChanged()
        })
    }

    override fun onStart() {
        super.onStart()
        Intent(this, SendingService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onPictureSendClicked(error : Error){
        showYesNoDialog(this, "Wysyłanie", "Spróbować ponownie wysłać obraz?", {
            lifecycleScope.launch(Dispatchers.IO) {
                db.errorsDAO().clearSource(error.source)
            }

            adapter.allItems.remove(error)
            adapter.filter()
            adapter.notifyDataSetChanged()

            if (!mService.isRunning) startService(Intent(this, SendingService::class.java))

            mService.addToQueue(error.getQueueItemExtra()!!)
        },{})

    }

    override fun onErrorRemoveClicked(error: Error) {
        showYesNoDialog(this, "Anulowanie", "Anulować wysyłanie tego obrazu?", {
            lifecycleScope.launch(Dispatchers.IO) {
                db.errorsDAO().clearSource(error.source)
            }
            adapter.allItems.remove(error)
            adapter.filter()
            adapter.notifyDataSetChanged()
        }, {})
    }
}