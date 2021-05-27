package pl.tysia.maggstone.ui.presentation_logic

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import pl.tysia.maggstone.R
import pl.tysia.maggstone.constants.ListActivityMode
import pl.tysia.maggstone.constants.MenuTileType
import pl.tysia.maggstone.data.NetworkChangeReceiver
import pl.tysia.maggstone.okDialog
import pl.tysia.maggstone.ui.DownloadStateActivity
import pl.tysia.maggstone.ui.contractors.ContractorListActivity
import pl.tysia.maggstone.ui.document.BasicNewDocumentActivity
import pl.tysia.maggstone.ui.document.NewShiftDocumentActivity
import pl.tysia.maggstone.ui.hose.HoseInfoActivity
import pl.tysia.maggstone.ui.main.MainActivity
import pl.tysia.maggstone.ui.orders.OrdersActivity
import pl.tysia.maggstone.ui.scanner.ShelfScannerActivity
import pl.tysia.maggstone.ui.scanner.WareScannerActivity
import pl.tysia.maggstone.ui.wares.WareListActivity


class MenuTile(private val type : MenuTileType, val activity : AppCompatActivity) : LinearLayout(activity) {

    init{
        val inflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        when(type) {
            MenuTileType.CATALOG_CONTACTORS -> inflater.inflate(
                R.layout.tile_contractors_catalog,
                this
            )
            MenuTileType.CATALOG_WARES -> inflater.inflate(R.layout.tile_wares_catalog, this)
            MenuTileType.DOCUMENT_OFFER -> inflater.inflate(R.layout.tile_document_offer, this)
            MenuTileType.DOCUMENT_PACKING -> inflater.inflate(R.layout.tile_document_packing, this)
            MenuTileType.DOCUMENT_RECEIVE -> inflater.inflate(R.layout.tile_document_recevie, this)
            MenuTileType.DOCUMENT_SHIFT -> inflater.inflate(R.layout.tile_document_shift, this)
            MenuTileType.FIND_HOSE -> inflater.inflate(R.layout.tile_find_hose, this)
            MenuTileType.CHANGE_LOCATION -> inflater.inflate(R.layout.tile_change_location, this)
            MenuTileType.SCAN_WARE -> inflater.inflate(R.layout.tile_scan_ware, this)
            MenuTileType.DOCUMENT_ORDER -> inflater.inflate(R.layout.tile_document_order, this)
        }

        setOnClickListener { onTileClicked() }

    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        val params = layoutParams as LayoutParams
        params.setMargins(24,16,24,16)

        layoutParams = params;
    }

    private fun onTileClicked(){
        when(type) {
            MenuTileType.CATALOG_CONTACTORS -> onContractorsCatalogClicked()
            MenuTileType.CATALOG_WARES -> onWareCatalogClicked()
            MenuTileType.DOCUMENT_OFFER -> onNewOfferClicked()
            MenuTileType.DOCUMENT_PACKING -> onOrdersClicked()
            MenuTileType.DOCUMENT_RECEIVE -> nic()
            MenuTileType.DOCUMENT_SHIFT -> onNewShiftClicked()
            MenuTileType.FIND_HOSE -> onSearchHoseClicked()
            MenuTileType.CHANGE_LOCATION -> onChangeLocationClicked()
            MenuTileType.SCAN_WARE -> onScanWareClicked()
            MenuTileType.DOCUMENT_ORDER -> onOrderClicked()
        }
    }

    fun nic() {
        Toast.makeText(activity, "Funkcja w przygotowaniu", Toast.LENGTH_LONG).show()
    }

    private fun onOrderClicked(){
        activity.startActivity(Intent(activity, WareListActivity::class.java).apply {
            putExtra(ListActivityMode.LIST_ACTIVITY_MODE_EXTRA, ListActivityMode.ORDER)
        })
    }

    private fun onSearchHoseClicked(){
        activity.startActivity(Intent(activity, HoseInfoActivity::class.java))
    }

    private fun onContractorsCatalogClicked(){
        activity.startActivity(Intent(activity, ContractorListActivity::class.java).apply {
            putExtra(ListActivityMode.LIST_ACTIVITY_MODE_EXTRA, ListActivityMode.BROWSE)
        })
    }

    private fun onWareCatalogClicked(){
        activity.startActivity(Intent(activity, WareListActivity::class.java).apply {
            putExtra(ListActivityMode.LIST_ACTIVITY_MODE_EXTRA, ListActivityMode.BROWSE)
        })
    }

    private fun onNewOfferClicked(){
        activity.startActivity(Intent(activity, BasicNewDocumentActivity::class.java))
    }

    private fun onNewShiftClicked(){
        activity.startActivity(Intent(activity, NewShiftDocumentActivity::class.java))
    }

    private fun onChangeLocationClicked(){
        if (NetworkChangeReceiver.internetConnection.value!!){
            activity.startActivity(Intent(activity, ShelfScannerActivity::class.java))
        }else {
            okDialog("Brak internetu", "Zmiana lokalizacji nie jest możliwa w trybie offline.", activity)
        }
    }

    private fun onOrdersClicked(){
        if (NetworkChangeReceiver.internetConnection.value!!){
            activity.startActivity(Intent(activity, OrdersActivity::class.java))
        }else {
            okDialog("Brak internetu", "Realizacja zamówień nie jest możliwa w trybie offline.", activity)
        }
    }

    private fun onScanWareClicked(){
        activity.startActivityForResult(Intent(activity, WareScannerActivity::class.java),
            MainActivity.WARE_REQUEST_CODE
        )
    }
}