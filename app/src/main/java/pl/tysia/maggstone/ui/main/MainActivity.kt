package pl.tysia.maggstone.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.tysia.maggstone.R
import pl.tysia.maggstone.constants.MenuTileType
import pl.tysia.maggstone.data.Database
import pl.tysia.maggstone.data.NetworkChangeReceiver
import pl.tysia.maggstone.data.model.Error
import pl.tysia.maggstone.data.model.UserAccessibilities
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
import pl.tysia.maggstone.ui.presentation_logic.MenuTile
import pl.tysia.maggstone.ui.scanner.ShelfScannerActivity
import pl.tysia.maggstone.ui.scanner.WareScannerActivity
import pl.tysia.maggstone.ui.showYesNoDialog
import pl.tysia.maggstone.ui.wares.WareInfoActivity



class MainActivity : AppCompatActivity() {
    companion object{
        const val WARE_REQUEST_CODE = 1
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

        val userAccessibilities = UserAccessibilities()

        userAccessibilities.accessibleTiles.forEach {
            addTile(it)
        }

    }

    private fun addTile(type : MenuTileType){
        when (type) {
            MenuTileType.DOCUMENT_SHIFT,
            MenuTileType.DOCUMENT_RECEIVE,
            MenuTileType.DOCUMENT_PACKING,
            MenuTileType.DOCUMENT_OFFER -> {
                section_documents.addView(MenuTile(type, this))
            }
            MenuTileType.CATALOG_WARES,
            MenuTileType.CATALOG_CONTACTORS-> {
                section_catalogs.addView(MenuTile(type, this))
            }
            MenuTileType.SCAN_WARE,
            MenuTileType.CHANGE_LOCATION,
            MenuTileType.FIND_HOSE-> {
                section_tools.addView(MenuTile(type, this))
            }
        }
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

    fun onSynchroniseClicked(view : View){
        startActivity(Intent(this, DownloadStateActivity::class.java))
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