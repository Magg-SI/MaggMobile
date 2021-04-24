package pl.tysia.maggstone.ui.warehouses

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import pl.tysia.maggstone.app.MaggApp
import pl.tysia.maggstone.data.model.Warehouse
import pl.tysia.maggstone.okDialog
import pl.tysia.maggstone.ui.presentation_logic.adapter.ICatalogable
import pl.tysia.maggstone.ui.simple_list.SimpleListActivity
import javax.inject.Inject

class WarehousesListActivity : SimpleListActivity() {
    @Inject lateinit var warehousesViewModel : WarehousesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showBlockingProgress(true)

        (application as MaggApp).appComponent.inject(this)

        warehousesViewModel.orders.observe(this@WarehousesListActivity, Observer {
            adapter.allItems.addAll(it as Collection<ICatalogable>)
            adapter.filter()
            adapter.notifyDataSetChanged()
            showBlockingProgress(false)
        })

        warehousesViewModel.warehousesResult.observe(this@WarehousesListActivity, Observer {
                okDialog("Błąd", getString(it), this)
                showBlockingProgress(false)
            })

        warehousesViewModel.getWarehouses()

    }

    override fun onItemSelected(item: ICatalogable) {
        val returnIntent = Intent()
        returnIntent.putExtra(Warehouse.WAREHOUSE_EXTRA, adapter.selectedItem as Warehouse)
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }
}