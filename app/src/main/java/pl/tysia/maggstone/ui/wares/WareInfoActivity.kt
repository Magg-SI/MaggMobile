package pl.tysia.maggstone.ui.wares

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_ware_info.*
import pl.tysia.maggstone.ui.picture.PictureEditorActivity
import pl.tysia.maggstone.R
import pl.tysia.maggstone.app.MaggApp
import pl.tysia.maggstone.data.model.Ware
import pl.tysia.maggstone.ui.BaseActivity
import javax.inject.Inject

class WareInfoActivity : BaseActivity() {
    private lateinit var ware : Ware

    @Inject lateinit var viewModel : WareInfoViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ware_info)

        (application as MaggApp).appComponent.inject(this)

        viewModel.availability.observe(this, Observer {
            ware.availabilities = it
            displayAvailability()
        })

        ware = intent.getSerializableExtra(Ware.WARE_EXTRA) as Ware

        displayWare()

        viewModel.getAvailabilities(ware.index!!)
    }

    private fun displayWare(){
        index_tv.text = ware.index
        name_tv.text = ware.name
        location_tv.text = ware.location
        if (ware.priceN != null && ware.priceB != null)
            price_tv.text =
                "netto: ${ware.priceN.toString()}"+
                "      brutto: ${ware.priceB.toString()}"
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