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
import pl.tysia.maggstone.data.model.Ware

class WareInfoActivity : AppCompatActivity() {
    private lateinit var ware : Ware
    private lateinit var viewModel : WareInfoViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ware_info)


        viewModel = ViewModelProvider(this, WareViewModelFactory())
            .get(WareInfoViewModel::class.java)

        viewModel.availability.observe(this, Observer {
            ware.availabilities = it
            displayAvailability()
            //TODO: showSendingState(false)
        })

        ware = intent.getSerializableExtra(Ware.WARE_EXTRA) as Ware

        displayWare()
    }

    private fun displayWare(){
        index_tv.text = ware.index
        name_tv.text = ware.name
        location_tv.text = ware.location
        price_tv.text = ware.price.toString()


    }

    private fun displayAvailability(){
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