package pl.tysia.maggstone.ui.hose

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_hose.*
import kotlinx.android.synthetic.main.activity_hose_info.*
import pl.tysia.maggstone.R
import pl.tysia.maggstone.app.MaggApp
import pl.tysia.maggstone.data.model.Hose
import pl.tysia.maggstone.ui.BaseActivity
import javax.inject.Inject

class HoseInfoActivity : BaseActivity() {
    @Inject lateinit var viewModel: HoseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as MaggApp).appComponent.inject(this)

        setContentView(R.layout.activity_hose_info)

        viewModel.hoseResult.observe(this@HoseInfoActivity, Observer {
            displayHose(it)
            showLoading(false)
            info_ll.visibility = View.VISIBLE
            availability_sv.visibility = View.VISIBLE
        })

        viewModel.result.observe(this@HoseInfoActivity, Observer {
            Toast.makeText(this@HoseInfoActivity, it, Toast.LENGTH_LONG).show()
            showLoading(false)
        })
    }

    fun onSearchClicked(view : View){
        val hoseNr = hose_nr.text.toString()

        if (hoseNr.isNotEmpty()){
            showLoading(true)

            viewModel.getHose(hoseNr)
        }
    }

    fun showLoading(show : Boolean){
        if (show) {
            info_ll.visibility = View.INVISIBLE
            availability_sv.visibility = View.INVISIBLE
        }

        showBlockingLoading(show)
    }

    fun displayHose(hose : Hose){
        name_tv.text = hose.name
        warehouse_tv.text = hose.warehouse
        date_tv.text = hose.documentDate
        number_tv.text = hose.documentNumber
        contractor_tv.text = hose.contractor
        quantity_tv2.text = hose.length.toString()
        price_tv.text = hose.priceN.toString()
        twist_tv.text = hose.angle
        maker_tv.text = hose.creator


    }
}