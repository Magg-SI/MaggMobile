package pl.tysia.maggstone.ui.service

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import pl.tysia.maggstone.app.MaggApp
import pl.tysia.maggstone.constants.Extras
import pl.tysia.maggstone.constants.ListActivityMode
import pl.tysia.maggstone.data.Database
import pl.tysia.maggstone.data.model.Contractor
import pl.tysia.maggstone.ui.cooperation.CooperationHistoryActivity
import pl.tysia.maggstone.ui.orders.OrdersViewModel
import pl.tysia.maggstone.ui.presentation_logic.adapter.ICatalogable
import pl.tysia.maggstone.ui.simple_list.SimpleListActivity
import javax.inject.Inject
import androidx.lifecycle.Observer

class ServicePriceListActivity : SimpleListActivity() {

    //@Inject lateinit var db : Database

    @Inject lateinit var priceListViewModel: ServicePriceListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        showBlockingLoading(true)

        (application as MaggApp).appComponent.inject(this)

        priceListViewModel.priceList.observe(this@ServicePriceListActivity, Observer {
            adapter.allItems.addAll(it)
            adapter.filter()
            adapter.notifyDataSetChanged()
            showBlockingLoading(false)
        })
    }

    override fun onResume() {
        super.onResume()
        adapter.allItems.clear()
        adapter.notifyDataSetChanged()

        priceListViewModel.getOrders()
    }
    override fun onItemSelected(item: ICatalogable) {
        /*if (mode == ListActivityMode.SELECT) {
            val returnIntent = Intent()
            returnIntent.putExtra(Contractor.CONTRACTOR_EXTRA, adapter.selectedItem as Contractor)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }else if (mode == ListActivityMode.BROWSE) {
            val intent = Intent(this, CooperationHistoryActivity::class.java)
            intent.putExtra(Extras.CONTRACTOR_EXTRA, adapter.selectedItem as Contractor)
            startActivity(intent)
        }*/
    }
}