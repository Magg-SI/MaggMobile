package pl.tysia.maggstone.ui.orders

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_orders.*
import pl.tysia.maggstone.R
import pl.tysia.maggstone.data.model.Order
import pl.tysia.maggstone.ui.login.afterTextChanged
import pl.tysia.maggstone.ui.presentation_logic.adapter.BasicCatalogAdapter
import pl.tysia.maggstone.ui.presentation_logic.adapter.CatalogAdapter
import pl.tysia.maggstone.ui.presentation_logic.adapter.OrdersAdapter
import pl.tysia.maggstone.ui.presentation_logic.filterer.StringFilter
import java.util.ArrayList

class OrdersActivity : AppCompatActivity(), CatalogAdapter.ItemSelectedListener<Order>{
    private lateinit var adapter : OrdersAdapter
    private lateinit var filter: StringFilter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders)

        adapter = OrdersAdapter(ArrayList())
        adapter.addItemSelectedListener(this)

        filter = StringFilter("")
        adapter.filterer.addFilter(filter)


        recycler.adapter = adapter

        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycler.layoutManager = linearLayoutManager
    }

    override fun onItemSelected(item: Order?) {
        //TODO: start OrderActivityOrderedWaresActivity
    }
}