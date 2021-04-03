package pl.tysia.maggstone.ui.orders

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_orders.*
import pl.tysia.maggstone.R
import pl.tysia.maggstone.data.NetAddressManager
import pl.tysia.maggstone.data.source.LoginDataSource
import pl.tysia.maggstone.data.source.LoginRepository
import pl.tysia.maggstone.data.model.Order
import pl.tysia.maggstone.ui.ViewModelFactory
import pl.tysia.maggstone.ui.presentation_logic.adapter.CatalogAdapter
import pl.tysia.maggstone.ui.presentation_logic.adapter.ICatalogable
import pl.tysia.maggstone.ui.presentation_logic.adapter.OrdersAdapter
import pl.tysia.maggstone.ui.presentation_logic.filterer.StringFilter
import pl.tysia.maggstone.ui.ware_ordering.OrderedWaresActivity
import java.util.ArrayList

class OrdersActivity : AppCompatActivity(), CatalogAdapter.ItemSelectedListener<Order>{
    private lateinit var adapter : OrdersAdapter
    private lateinit var filter: StringFilter<ICatalogable>

    private lateinit var ordersViewModel: OrdersViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders)

        ordersViewModel = ViewModelProvider(this,
            ViewModelFactory(this)
        ).get(OrdersViewModel::class.java)


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

        ordersViewModel.getOrders( LoginRepository(
            LoginDataSource(NetAddressManager(this)),
            this
        ).user!!.token)
    }

    override fun onItemSelected(item: Order) {
        val intent = Intent(this, OrderedWaresActivity::class.java)
        intent.putExtra(Order.ORDER_EXTRA, item)
        startActivity(intent)
    }
}