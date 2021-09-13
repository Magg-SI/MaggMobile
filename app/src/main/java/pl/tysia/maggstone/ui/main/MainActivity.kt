package pl.tysia.maggstone.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_main.*
import pl.tysia.maggstone.R
import pl.tysia.maggstone.app.MaggApp
import pl.tysia.maggstone.constants.AccessibleFunctionsTypes
import pl.tysia.maggstone.constants.Extras
import pl.tysia.maggstone.data.Database
import pl.tysia.maggstone.data.NetworkChangeReceiver
import pl.tysia.maggstone.data.logic.UserAccessLogic
import pl.tysia.maggstone.data.model.Ware
import pl.tysia.maggstone.data.service.ContractorsDownloadService
import pl.tysia.maggstone.data.service.WaresDownloadService
import pl.tysia.maggstone.ui.*
import pl.tysia.maggstone.ui.error.ErrorsActivity
import pl.tysia.maggstone.ui.presentation_logic.MenuTile
import pl.tysia.maggstone.ui.wares.WareInfoActivity
import javax.inject.Inject

class MainActivity : BaseActivity() {
    companion object{
        const val WARE_REQUEST_CODE = 1
    }

    @Inject lateinit var db : Database
    @Inject lateinit var userAccessLogic : UserAccessLogic

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        (application as MaggApp).appComponent.inject(this)

        startService(Intent(this, WaresDownloadService::class.java))
        startService(Intent(this, ContractorsDownloadService::class.java))

        userAccessLogic.getAccessibleFunctions().getAccessibleMenuTiles().forEach {
            addTile(it)
        }
    }

    private fun addTile(type : AccessibleFunctionsTypes){
        when (type) {
            AccessibleFunctionsTypes.MENU_TILE_DOCUMENT_SHIFT,
            AccessibleFunctionsTypes.MENU_TILE_DOCUMENT_RECEIVE,
            AccessibleFunctionsTypes.MENU_TILE_DOCUMENT_PACKING,
            AccessibleFunctionsTypes.MENU_TILE_DOCUMENT_ORDER,
            AccessibleFunctionsTypes.MENU_TILE_STOCKTAKING,
            AccessibleFunctionsTypes.MENU_TILE_DOCUMENT_OFFER -> {
                section_documents.addView(MenuTile(type, this))
            }
            AccessibleFunctionsTypes.MENU_TILE_CATALOG_WARES,
            AccessibleFunctionsTypes.MENU_TILE_CATALOG_CONTACTORS-> {
                section_catalogs.addView(MenuTile(type, this))
            }
            AccessibleFunctionsTypes.MENU_TILE_SCAN_WARE,
            AccessibleFunctionsTypes.MENU_TILE_CHANGE_LOCATION,
            AccessibleFunctionsTypes.MENU_TILE_FIND_HOSE-> {
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
            intent.putExtra(Extras.CALLING_ACTIVITY, this::class.java.simpleName)
            startActivity(intent)
        }
    }
}