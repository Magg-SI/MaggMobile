package pl.tysia.maggstone.ui.ware_ordering

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_ordered_wares.*
import pl.tysia.maggstone.R
import pl.tysia.maggstone.data.NetAddressManager
import pl.tysia.maggstone.data.source.LoginDataSource
import pl.tysia.maggstone.data.source.LoginRepository
import pl.tysia.maggstone.data.model.Order
import pl.tysia.maggstone.data.model.Ware
import pl.tysia.maggstone.ui.ViewModelFactory
import pl.tysia.maggstone.ui.orders.OrderedWaresViewModel
import pl.tysia.maggstone.ui.presentation_logic.adapter.CatalogAdapter
import pl.tysia.maggstone.ui.presentation_logic.adapter.ICatalogable
import pl.tysia.maggstone.ui.presentation_logic.adapter.OrderedItemsAdapter
import pl.tysia.maggstone.ui.presentation_logic.filterer.StringFilter
import pl.tysia.maggstone.ui.scanner.WareScannerActivity
import java.util.ArrayList



class OrderedWaresActivity : AppCompatActivity() , CatalogAdapter.ItemSelectedListener<Ware>,
    TextWatcher {
    private lateinit var adapter:  OrderedItemsAdapter
    private var filter: StringFilter<ICatalogable>? = null

    private lateinit var recyclerView: RecyclerView

    private lateinit var orderedWaresViewModel: OrderedWaresViewModel

    private lateinit var order : Order

    companion object{
        private const val WARE_REQUEST_CODE  = 36985
        private const val PACK_REQUEST = 124

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ordered_wares)

        order = intent.getSerializableExtra(Order.ORDER_EXTRA) as Order

        orderedWaresViewModel = ViewModelProvider(this,
            ViewModelFactory(this)
        ).get(OrderedWaresViewModel::class.java)


        adapter =
            OrderedItemsAdapter(
                ArrayList()
            )

        adapter.addItemSelectedListener(this)

        val filterer = adapter.filterer
        filter = StringFilter(null){ filteredString, item ->
            item.title.toLowerCase().contains(filteredString.toLowerCase())
        }

        filterer.addFilter(filter!!)

        recyclerView = findViewById(R.id.wares_recycler)

        recyclerView.adapter = adapter

        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager

        order_desc_tv.text = order.description
        order_title_tv.text = order.title

        orderedWaresViewModel.wares.observe(this@OrderedWaresActivity, Observer {
            adapter.allItems.addAll(it)
            adapter.filter()
            adapter.notifyDataSetChanged()
            showProgress(false)
        })

        orderedWaresViewModel.getOrder( LoginRepository(
            LoginDataSource(NetAddressManager(this)),
            this
        ).user!!.token, order.id)
        showProgress(true)
    }

    override fun onItemSelected(item: Ware?) {
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
        filter?.filteredString = s.toString()
        adapter.filter()
        adapter.notifyDataSetChanged()

    }

    public fun onScanWareClicked(view: View){
        val intent = Intent(this, WareScannerActivity::class.java)
        startActivityForResult(intent,
            WARE_REQUEST_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == WARE_REQUEST_CODE){
            val ware = data!!.getSerializableExtra(Ware.WARE_EXTRA) as Ware

            adapter.allItems.forEach { orderedWare ->
                if (orderedWare.qrCode == ware.qrCode)
                    onItemSelected(orderedWare)
            }
        }else if (resultCode == Activity.RESULT_OK && requestCode == PACK_REQUEST){
            adapter.selectedItem.packed = true
            adapter.notifyDataSetChanged()

        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private fun showProgress(show: Boolean) {
        val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()


        form.visibility = if (show) View.GONE else View.VISIBLE
        form.animate()
            .setDuration(shortAnimTime)
            .alpha((if (show) 0 else 1).toFloat())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    form.visibility = if (show) View.GONE else View.VISIBLE
                }
            })

        progressBar.visibility = if (show) View.VISIBLE else View.GONE
        progressBar.animate()
            .setDuration(shortAnimTime)
            .alpha((if (show) 1 else 0).toFloat())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    progressBar.visibility = if (show) View.VISIBLE else View.GONE
                }
            })
    }

}
