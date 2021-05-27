package pl.tysia.maggstone.ui.wares

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_ware_info.*
import kotlinx.android.synthetic.main.content_ware_details.*
import pl.tysia.maggstone.ui.picture.PictureEditorActivity
import pl.tysia.maggstone.R
import pl.tysia.maggstone.app.MaggApp
import pl.tysia.maggstone.constants.Extras
import pl.tysia.maggstone.data.model.Ware
import pl.tysia.maggstone.ui.BaseActivity
import pl.tysia.maggstone.ui.main.MainActivity
import javax.inject.Inject

open class WareInfoActivity : BaseActivity() {
    protected lateinit var ware : Ware

    @Inject lateinit var viewModel : WareInfoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        val callingActivity = intent.getStringExtra(Extras.CALLING_ACTIVITY)
        setTheme(callingActivity!!)

        super.onCreate(savedInstanceState)

        setLayout()

        (application as MaggApp).appComponent.inject(this)

        setObservers()

        ware = intent.getSerializableExtra(Ware.WARE_EXTRA) as Ware

        displayWare()

        viewModel.getAvailabilities(ware.index!!)

        if (ware.photoID!=null)
            viewModel.getSmallPicture(ware.photoID!!)
    }

    protected open fun setLayout(){
        setContentView(R.layout.activity_ware_info)
    }

    protected open fun setObservers(){
        viewModel.availability.observe(this, {
            ware.availabilities = it
            displayAvailability()
        })

        viewModel.photo.observe(this, {
            picture_image_view.scaleType = ImageView.ScaleType.FIT_XY
            picture_image_view.setImageBitmap(it)
        })
    }

    private fun setTheme(callingActivity : String){
        when(callingActivity){
            MainActivity::class.java.simpleName ->  setTheme(R.style.AppTheme);
            WareListActivity::class.java.simpleName ->  setTheme(R.style.AppThemeOrange);
        }
    }

    private fun displayWare(){
        index_tv.text = ware.index
        name_tv.text = ware.name
        location_tv.text = ware.location
        if (ware.priceN != null && ware.priceB != null)
            price_tv.text =
                "netto:  ${ware.priceN.toString()}\n"+
                "brutto: ${ware.priceB.toString()}"
    }

    protected fun displayAvailability(){
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