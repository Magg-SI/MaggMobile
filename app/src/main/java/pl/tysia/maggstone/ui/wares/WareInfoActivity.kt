package pl.tysia.maggstone.ui.wares

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_ware_info.*
import pl.tysia.maggstone.ui.picture.PictureEditorActivity
import pl.tysia.maggstone.R
import pl.tysia.maggstone.constants.ListActivityMode
import pl.tysia.maggstone.data.NetAddressManager
import pl.tysia.maggstone.data.model.Ware
import pl.tysia.maggstone.data.source.LoginDataSource
import pl.tysia.maggstone.data.source.LoginRepository
import pl.tysia.maggstone.ui.ViewModelFactory

class WareInfoActivity : AppCompatActivity() {
    private lateinit var ware : Ware
    private lateinit var viewModel : WareInfoViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ware_info)

        viewModel = ViewModelProvider(this, ViewModelFactory(this))
            .get(WareInfoViewModel::class.java)

        viewModel.availability.observe(this, Observer {
            ware.availabilities = it
            displayAvailability()
        })

        ware = intent.getSerializableExtra(Ware.WARE_EXTRA) as Ware

        displayWare()


        val token = LoginRepository(
            LoginDataSource(NetAddressManager(this)),
            this
        ).user!!.token

        viewModel.getAvailabilities(ware.index!!, token)
    }

    private fun displayWare(){
        index_tv.text = ware.index
        name_tv.text = ware.name
        location_tv.text = ware.location
        if (ware.priceN != null && ware.priceB != null)
            price_tv.text =
                "Cena netto: ${ware.priceN.toString()}" +
                    "Cena brutto: ${ware.priceB.toString()}"

    }

    private fun displayAvailability(){
        availability_ll.removeAllViews()
        val stringBuilder = StringBuilder()

        for (availability in ware.availabilities!!){
            val textView = TextView(this)

            stringBuilder.append(availability.warehouse)
            stringBuilder.append(": ")
            stringBuilder.append(availability.quantity)

            textView.text = stringBuilder.toString()
            textView.textSize = 16f

            availability_ll.addView(textView)

            val param = textView.layoutParams as ViewGroup.MarginLayoutParams
            param.setMargins(0,8,8,8)
            textView.layoutParams = param

            stringBuilder.clear()

        }
    }

    fun onChangePictureClick(view : View){
        val intent = Intent(this, PictureEditorActivity::class.java)
        intent.putExtra(Ware.WARE_EXTRA,ware)
        startActivity(intent)
    }

}