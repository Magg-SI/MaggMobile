package pl.tysia.maggstone.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.tysia.maggstone.R
import pl.tysia.maggstone.data.Database
import pl.tysia.maggstone.data.NetworkChangeReceiver
import pl.tysia.maggstone.data.model.Error
import pl.tysia.maggstone.data.model.Ware
import pl.tysia.maggstone.data.service.ContractorsDownloadService
import pl.tysia.maggstone.data.service.WaresDownloadService
import pl.tysia.maggstone.okDialog
import pl.tysia.maggstone.ui.DownloadStateActivity
import pl.tysia.maggstone.ui.SendingStateActivity
import pl.tysia.maggstone.ui.SettingsActivity
import pl.tysia.maggstone.ui.document.BasicNewDocumentActivity
import pl.tysia.maggstone.ui.error.ErrorsActivity
import pl.tysia.maggstone.ui.orders.OrdersActivity
import pl.tysia.maggstone.ui.scanner.ShelfScannerActivity
import pl.tysia.maggstone.ui.scanner.WareScannerActivity
import pl.tysia.maggstone.ui.showYesNoDialog
import pl.tysia.maggstone.ui.wares.WareInfoActivity



class MainActivity : AppCompatActivity() {
    companion object{
        private const val WARE_REQUEST_CODE = 1
    }

    private lateinit var db : Database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        startService(Intent(this, WaresDownloadService::class.java))
        startService(Intent(this, ContractorsDownloadService::class.java))

        db = Room.databaseBuilder(
            this,
            Database::class.java, "pl.tysia.database"
        ).build()


    }


    override fun onResume() {
        super.onResume()

        NetworkChangeReceiver.internetConnection.observeForever {
            db.errorsDAO().getObservableNumber().observe(this, Observer{
                if (it > 0) error_fab.visibility = View.VISIBLE
                else error_fab.visibility = View.GONE
            })
        }
    }

    fun onNewDocumentClicked(view : View){
        startActivity(Intent(this, BasicNewDocumentActivity::class.java))
    }

    fun onChangeLocationClicked(view : View){
        if (NetworkChangeReceiver.internetConnection.value!!){
            startActivity(Intent(this, ShelfScannerActivity::class.java))
        }else {
            okDialog("Brak internetu", "Zmiana lokalizacji nie jest możliwa w trybie offline.", this)
        }
    }

    fun onSynchroniseClicked(view : View){
        startActivity(Intent(this, DownloadStateActivity::class.java))
    }

    fun onOrdersClicked(view : View){
        if (NetworkChangeReceiver.internetConnection.value!!){
            startActivity(Intent(this, OrdersActivity::class.java))
        }else {
            okDialog("Brak internetu", "Realizacja zamówień nie jest możliwa w trybie offline.", this)
        }
    }

    fun onScanWareClicked(view : View){
        startActivityForResult(Intent(this, WareScannerActivity::class.java),
            WARE_REQUEST_CODE
        )
    }

    fun onSettingsClicked(view: View){
        startActivity(Intent(this, SettingsActivity::class.java))
    }

    fun onSendingClicked(view: View){
        startActivity(Intent(this, SendingStateActivity::class.java))
    }

    fun onErrorsClicked(view: View){
        startActivity(Intent(this, ErrorsActivity::class.java))
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == WARE_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            val ware = data!!.getSerializableExtra(Ware.WARE_EXTRA) as Ware

            val intent = Intent(this, WareInfoActivity::class.java)
            intent.putExtra(Ware.WARE_EXTRA, ware)
            startActivity(intent)
        }
    }
}