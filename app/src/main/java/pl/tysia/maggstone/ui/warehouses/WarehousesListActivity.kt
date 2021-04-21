package pl.tysia.maggstone.ui.warehouses

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import pl.tysia.maggstone.data.Database
import pl.tysia.maggstone.data.NetAddressManager
import pl.tysia.maggstone.data.model.Warehouse
import pl.tysia.maggstone.data.source.LoginDataSource
import pl.tysia.maggstone.data.source.LoginRepository
import pl.tysia.maggstone.ui.ViewModelFactory
import pl.tysia.maggstone.ui.orders.OrdersViewModel
import pl.tysia.maggstone.ui.presentation_logic.adapter.ICatalogable
import pl.tysia.maggstone.ui.simple_list.SimpleListActivity

class WarehousesListActivity : SimpleListActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showBlockingProgress(true)

         val warehousesViewModel = ViewModelProvider(this,
            ViewModelFactory(this)
        ).get(WarehousesViewModel::class.java)


        warehousesViewModel.orders.observe(this@WarehousesListActivity, Observer {
            adapter.allItems.addAll(it as Collection<ICatalogable>)
            adapter.filter()
            adapter.notifyDataSetChanged()
            showBlockingProgress(false)
        })

        warehousesViewModel.getWarehouses( LoginRepository(
            LoginDataSource(NetAddressManager(this)),
            this
        ).user!!.token)

    }

    override fun onItemSelected(item: ICatalogable) {
        val returnIntent = Intent()
        returnIntent.putExtra(Warehouse.WAREHOUSE_EXTRA, adapter.selectedItem as Warehouse)
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }
}