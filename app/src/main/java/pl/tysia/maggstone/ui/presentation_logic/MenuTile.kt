package pl.tysia.maggstone.ui.presentation_logic

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pl.tysia.maggstone.R
import pl.tysia.maggstone.constants.AccessibleFunctionsTypes
import pl.tysia.maggstone.constants.ListActivityMode
import pl.tysia.maggstone.data.NetworkChangeReceiver
import pl.tysia.maggstone.okDialog
import pl.tysia.maggstone.ui.contractors.ContractorListActivity
import pl.tysia.maggstone.ui.document.BasicNewDocumentActivity
import pl.tysia.maggstone.ui.document.NewShiftDocumentActivity
import pl.tysia.maggstone.ui.hose.HoseInfoActivity
import pl.tysia.maggstone.ui.main.MainActivity
import pl.tysia.maggstone.ui.orders.OrdersActivity
import pl.tysia.maggstone.ui.scanner.ShelfScannerActivity
import pl.tysia.maggstone.ui.scanner.WareScannerActivity
import pl.tysia.maggstone.ui.service.ServicePriceListActivity
import pl.tysia.maggstone.ui.stocktaking.StocktakingActivity
import pl.tysia.maggstone.ui.wares.WareListActivity


class MenuTile(private val type : AccessibleFunctionsTypes, val activity : AppCompatActivity) : LinearLayout(activity) {

    init{
        val inflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        when(type) {
            AccessibleFunctionsTypes.MENU_TILE_CATALOG_CONTACTORS -> inflater.inflate(
                R.layout.tile_contractors_catalog,
                this
            )
            AccessibleFunctionsTypes.MENU_TILE_CATALOG_WARES -> inflater.inflate(R.layout.tile_wares_catalog, this)
            AccessibleFunctionsTypes.MENU_TILE_DOCUMENT_OFFER -> inflater.inflate(R.layout.tile_document_offer, this)
            AccessibleFunctionsTypes.MENU_TILE_DOCUMENT_PACKING -> inflater.inflate(R.layout.tile_document_packing, this)
            AccessibleFunctionsTypes.MENU_TILE_DOCUMENT_RECEIVE -> inflater.inflate(R.layout.tile_document_recevie, this)
            AccessibleFunctionsTypes.MENU_TILE_DOCUMENT_SHIFT -> inflater.inflate(R.layout.tile_document_shift, this)
            AccessibleFunctionsTypes.MENU_TILE_FIND_HOSE -> inflater.inflate(R.layout.tile_find_hose, this)
            AccessibleFunctionsTypes.MENU_TILE_CHANGE_LOCATION -> inflater.inflate(R.layout.tile_change_location, this)
            AccessibleFunctionsTypes.MENU_TILE_SCAN_WARE -> inflater.inflate(R.layout.tile_scan_ware, this)
            AccessibleFunctionsTypes.MENU_TILE_DOCUMENT_ORDER -> inflater.inflate(R.layout.tile_document_order, this)
            AccessibleFunctionsTypes.MENU_TILE_STOCKTAKING -> inflater.inflate(R.layout.tile_stocktaking, this)
            AccessibleFunctionsTypes.MENU_TILE_PRICE_LIST -> inflater.inflate(R.layout.tile_price_list, this)
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
            AccessibleFunctionsTypes.MENU_TILE_CATALOG_CONTACTORS -> onContractorsCatalogClicked()
            AccessibleFunctionsTypes.MENU_TILE_CATALOG_WARES -> onWareCatalogClicked()
            AccessibleFunctionsTypes.MENU_TILE_DOCUMENT_OFFER -> onNewOfferClicked()
            AccessibleFunctionsTypes.MENU_TILE_DOCUMENT_PACKING -> onOrdersClicked()
            AccessibleFunctionsTypes.MENU_TILE_DOCUMENT_RECEIVE -> nic()
            AccessibleFunctionsTypes.MENU_TILE_DOCUMENT_SHIFT -> onNewShiftClicked()
            AccessibleFunctionsTypes.MENU_TILE_FIND_HOSE -> onSearchHoseClicked()
            AccessibleFunctionsTypes.MENU_TILE_CHANGE_LOCATION -> onChangeLocationClicked()
            AccessibleFunctionsTypes.MENU_TILE_SCAN_WARE -> onScanWareClicked()
            AccessibleFunctionsTypes.MENU_TILE_DOCUMENT_ORDER -> onOrderClicked()
            AccessibleFunctionsTypes.MENU_TILE_STOCKTAKING -> onStocktakingClicked()
            AccessibleFunctionsTypes.MENU_TILE_PRICE_LIST -> onPriceListClicked()
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

    private fun onPriceListClicked(){
        if (NetworkChangeReceiver.internetConnection.value!!){
            activity.startActivity(Intent(activity, ServicePriceListActivity::class.java))
        }else {
            okDialog("Brak internetu", "Cennik serwisu nie jest dostępny w trybie offline.", activity)
        }
    }

    private fun onStocktakingClicked(){
        activity.startActivity(Intent(activity, StocktakingActivity::class.java))
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