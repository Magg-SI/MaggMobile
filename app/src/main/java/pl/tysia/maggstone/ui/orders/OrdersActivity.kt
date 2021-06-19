package pl.tysia.maggstone.ui.orders

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_orders.*
import pl.tysia.maggstone.R
import pl.tysia.maggstone.app.MaggApp
import pl.tysia.maggstone.constants.Extras
import pl.tysia.maggstone.data.model.Order
import pl.tysia.maggstone.data.model.Ware
import pl.tysia.maggstone.ui.BaseActivity
import pl.tysia.maggstone.ui.main.MainActivity
import pl.tysia.maggstone.ui.presentation_logic.adapter.CatalogAdapter
import pl.tysia.maggstone.ui.presentation_logic.adapter.ICatalogable
import pl.tysia.maggstone.ui.presentation_logic.adapter.OrdersAdapter
import pl.tysia.maggstone.ui.presentation_logic.filterer.StringFilter
import pl.tysia.maggstone.ui.ware_ordering.OrderedWaresActivity
import pl.tysia.maggstone.ui.wares.WareInfoActivity
import java.util.ArrayList
import javax.inject.Inject

class OrdersActivity : BaseActivity(), CatalogAdapter.ItemSelectedListener<Order>{
    private lateinit var adapter : OrdersAdapter
    private lateinit var filter: StringFilter<ICatalogable>

    @Inject lateinit var ordersViewModel: OrdersViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders)

        (application as MaggApp).appComponent.inject(this)

        adapter = OrdersAdapter(ArrayList())
        adapter.addItemSelectedListener(this)


        filter = StringFilter()
        adapter.filterer.addFilter(filter)

        recycler.adapter = adapter


        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycler.layoutManager = linearLayoutManager

        ordersViewModel.orders.observe(this@OrdersActivity, Observer {
            adapter.allItems.addAll(it)
            adapter.filter()
            adapter.notifyDataSetChanged()
        })

    }

    override fun onResume() {
        super.onResume()
        adapter.allItems.clear()
        adapter.notifyDataSetChanged()

        ordersViewModel.getOrders()
    }

    override fun onItemSelected(item: Order) {
        val intent = Intent(this, OrderedWaresActivity::class.java)
        intent.putExtra(Order.ORDER_EXTRA, item)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == MainActivity.WARE_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            val ware = data!!.getSerializableExtra(Ware.WARE_EXTRA) as Ware

            val intent = Intent(this, WareInfoActivity::class.java)
            intent.putExtra(Ware.WARE_EXTRA, ware)
            intent.putExtra(Extras.CALLING_ACTIVITY, this::class.java.simpleName)
            startActivity(intent)
        }
    }
}