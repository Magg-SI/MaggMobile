package pl.tysia.maggstone.ui.wares

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_ware_info.*
import pl.tysia.maggstone.ui.PictureEditorActivity
import pl.tysia.maggstone.R
import pl.tysia.maggstone.data.model.Ware

class WareInfoActivity : AppCompatActivity() {
    private lateinit var ware : Ware

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ware_info)

        ware = intent.getSerializableExtra(Ware.WARE_EXTRA) as Ware

        displayWare()
    }

    private fun displayWare(){
        index_tv.text = ware.index
        name_tv.text = ware.name
        location_tv.text = ware.location
        price_tv.text = ware.price.toString()
    }

    fun onChangePictureClick(view : View){
        val intent = Intent(this, PictureEditorActivity::class.java)
        intent.putExtra(Ware.WARE_EXTRA,ware)
        startActivity(intent)
    }
}