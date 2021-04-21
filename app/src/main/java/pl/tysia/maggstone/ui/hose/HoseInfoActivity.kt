package pl.tysia.maggstone.ui.hose

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_hose.*
import kotlinx.android.synthetic.main.activity_hose_info.*
import pl.tysia.maggstone.R
import pl.tysia.maggstone.constants.Extras
import pl.tysia.maggstone.data.NetAddressManager
import pl.tysia.maggstone.data.model.Hose
import pl.tysia.maggstone.data.source.LoginDataSource
import pl.tysia.maggstone.data.source.LoginRepository
import pl.tysia.maggstone.ui.BaseActivity
import pl.tysia.maggstone.ui.ViewModelFactory
import pl.tysia.maggstone.ui.login.afterTextChanged

class HoseInfoActivity : BaseActivity() {
    private lateinit var viewModel: HoseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_hose_info)

        viewModel = ViewModelProvider(this,
            ViewModelFactory(this)
        ).get(HoseViewModel::class.java)

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

            val token = LoginRepository(
                LoginDataSource(NetAddressManager(this)),
                this
            ).user!!.token
            viewModel.getHose(token, hoseNr)
        }
    }

    fun showLoading(show : Boolean){
        if (show) {
            info_ll.visibility = View.INVISIBLE
            availability_sv.visibility = View.INVISIBLE
        }

        showBlockingProgress(show)
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