package pl.tysia.maggstone.ui.hose

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_hose_info.*
import pl.tysia.maggstone.R
import pl.tysia.maggstone.constants.Extras
import pl.tysia.maggstone.data.model.Hose

class HoseInfoActivity : AppCompatActivity() {

    lateinit var hose : Hose

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_hose_info)
        hose = intent.getSerializableExtra(Extras.HOSE_EXTRA) as Hose

        displayHose()
    }
    fun displayHose(){
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