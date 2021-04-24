package pl.tysia.maggstone.ui.orders

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_orders.*
import pl.tysia.maggstone.R
import pl.tysia.maggstone.app.MaggApp
import pl.tysia.maggstone.data.model.Order
import pl.tysia.maggstone.ui.BaseActivity
import pl.tysia.maggstone.ui.presentation_logic.adapter.CatalogAdapter
import pl.tysia.maggstone.ui.presentation_logic.adapter.ICatalogable
import pl.tysia.maggstone.ui.presentation_logic.adapter.OrdersAdapter
import pl.tysia.maggstone.ui.presentation_logic.filterer.StringFilter
import pl.tysia.maggstone.ui.ware_ordering.OrderedWaresActivity
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


        filter = StringFilter(null){ filteredStrings, item ->
            filteredStrings.count { item.getFilteredValue()!!.toLowerCase().contains(it.toLowerCase()) }
        }

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
}