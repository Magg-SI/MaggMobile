package pl.tysia.maggstone.ui.ware_ordering

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_ordered_wares.*
import pl.tysia.maggstone.R
import pl.tysia.maggstone.app.MaggApp
import pl.tysia.maggstone.data.model.Order
import pl.tysia.maggstone.data.model.OrderedWare
import pl.tysia.maggstone.data.model.Ware
import pl.tysia.maggstone.ui.BaseActivity
import pl.tysia.maggstone.ui.orders.OrderedWaresViewModel
import pl.tysia.maggstone.ui.presentation_logic.adapter.CatalogAdapter
import pl.tysia.maggstone.ui.presentation_logic.adapter.ICatalogable
import pl.tysia.maggstone.ui.presentation_logic.adapter.OrderedItemsAdapter
import pl.tysia.maggstone.ui.presentation_logic.filterer.StringFilter
import pl.tysia.maggstone.ui.scanner.WareScannerActivity
import java.util.ArrayList
import javax.inject.Inject


class OrderedWaresActivity : BaseActivity() , CatalogAdapter.ItemSelectedListener<OrderedWare>,
    TextWatcher {
    private lateinit var adapter:  OrderedItemsAdapter
    private var filter: StringFilter<ICatalogable>? = null

    private lateinit var recyclerView: RecyclerView

    @Inject lateinit var orderedWaresViewModel: OrderedWaresViewModel

    private lateinit var order : Order

    companion object{
        private const val WARE_REQUEST_CODE  = 36985
        private const val PACK_REQUEST = 124

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ordered_wares)

        (application as MaggApp).appComponent.inject(this)

        order = intent.getSerializableExtra(Order.ORDER_EXTRA) as Order

        adapter =
            OrderedItemsAdapter(
                ArrayList()
            )

        adapter.addItemSelectedListener(this)

        val filterer = adapter.filterer
        filter = StringFilter(null){ filteredStrings, item ->
            filteredStrings.count { item.getFilteredValue().toLowerCase().contains(it.toLowerCase()) }
        }

        filterer.addFilter(filter!!)

        recyclerView = findViewById(R.id.wares_recycler)

        recyclerView.adapter = adapter

        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager

        order_desc_tv.text = order.getDescription()
        order_title_tv.text = order.getTitle()

        orderedWaresViewModel.wares.observe(this@OrderedWaresActivity, Observer {
            adapter.allItems.addAll(it)
            adapter.filter()
            adapter.notifyDataSetChanged()
            showBlockingProgress(false)
        })

        orderedWaresViewModel.getOrder(order.id)
        showBlockingProgress(true)
    }

    override fun onItemSelected(item: OrderedWare) {
        val intent = Intent(this@OrderedWaresActivity, WareOrderingActivity::class.java)
        intent.putExtra(Ware.WARE_EXTRA, item)
        startActivityForResult(intent,
            PACK_REQUEST
        )
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

    }

    override fun afterTextChanged(s: Editable) {
        filter?.filteredStrings = s.toString().split(" ")
        adapter.filter()
        adapter.notifyDataSetChanged()

    }

    fun onScanWareClicked(view: View){
        val intent = Intent(this, WareScannerActivity::class.java)
        startActivityForResult(intent,
            WARE_REQUEST_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == WARE_REQUEST_CODE){
            val ware = data!!.getSerializableExtra(Ware.WARE_EXTRA) as Ware

            val foundWare = adapter.allItems.firstOrNull { it.qrCode == ware.qrCode }
            adapter.selectedItem = foundWare

            if (foundWare != null)
                onItemSelected(foundWare)
            else
                Toast.makeText(this, "Nie znaleziono takiego towaru na liście" , Toast.LENGTH_LONG)

        }else if (resultCode == Activity.RESULT_OK && requestCode == PACK_REQUEST){
            val ware = data!!.getSerializableExtra(Ware.WARE_EXTRA) as OrderedWare
            adapter.selectedItem!!.copyNumbers(ware)
            adapter.notifyDataSetChanged()

        }
    }

}
