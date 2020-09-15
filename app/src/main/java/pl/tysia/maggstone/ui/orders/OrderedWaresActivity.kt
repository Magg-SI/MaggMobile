package pl.tysia.maggstone.ui.orders

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pl.tysia.maggstone.ui.presentation_logic.adapter.DocumentAdapter
import kotlinx.android.synthetic.main.activity_ordered_wares.*
import pl.tysia.maggstone.R
import pl.tysia.maggstone.data.model.DocumentItem
import pl.tysia.maggstone.data.model.Order
import pl.tysia.maggstone.data.model.Ware
import pl.tysia.maggstone.ui.presentation_logic.adapter.BasicCatalogAdapter
import pl.tysia.maggstone.ui.presentation_logic.adapter.CatalogAdapter
import pl.tysia.maggstone.ui.presentation_logic.adapter.ICatalogable
import pl.tysia.maggstone.ui.presentation_logic.filterer.StringFilter
import pl.tysia.maggstone.ui.scanner.WareScannerActivity
import java.io.IOException
import java.util.ArrayList



class OrderedWaresActivity : AppCompatActivity() , CatalogAdapter.ItemSelectedListener<Ware>,
    TextWatcher {
    private lateinit var adapter: DocumentAdapter
    private var filter: StringFilter? = null

    private lateinit var recyclerView: RecyclerView


    private lateinit var order : Order

    companion object{
        const val ORDER = "pl.tysia.maggwarehouse.order"

        private const val WARE_REQUEST_CODE  = 36985
        private const val PACK_REQUEST = 124

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ordered_wares)

        order = intent.getSerializableExtra(ORDER) as Order

        adapter =
            DocumentAdapter(
                ArrayList()
            )
        adapter.addItemSelectedListener(this)

        val filterer = adapter.filterer
        filter = StringFilter(null)
        filterer.addFilter(filter!!)

        recyclerView = findViewById(R.id.wares_recycler)

        recyclerView.adapter = adapter

        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager

        order_desc_tv.text = order.shortDescription
        order_title_tv.text = order.title


    }

    override fun onItemSelected(item: Ware?) {
        val intent = Intent(this@OrderedWaresActivity, WareOrderingActivity::class.java)
        intent.putExtra(Ware.WARE_EXTRA, item)
        startActivityForResult(intent, PACK_REQUEST)
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
        startActivityForResult(intent, WARE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == WARE_REQUEST_CODE){
            val ware = data!!.getSerializableExtra(Ware.WARE_EXTRA) as Ware

            adapter.allItems.forEach { orderedWare ->
                val iWare = (orderedWare as DocumentItem.OrderedWareItem).item as Ware
                if (iWare.qrCode == ware.qrCode)
                    onItemSelected(iWare)
            }
        }else if (resultCode == Activity.RESULT_OK && requestCode == PACK_REQUEST){
            (adapter.selectedItem as DocumentItem.OrderedWareItem).packed = true
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
