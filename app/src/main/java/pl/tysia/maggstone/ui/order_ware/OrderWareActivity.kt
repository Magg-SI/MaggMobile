package pl.tysia.maggstone.ui.order_ware

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_order_ware.*
import pl.tysia.maggstone.R
import pl.tysia.maggstone.okDialog
import pl.tysia.maggstone.ui.hose.afterTextChanged
import pl.tysia.maggstone.ui.main.MainActivity
import pl.tysia.maggstone.ui.wares.WareInfoActivity

class OrderWareActivity : WareInfoActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        number_tv.afterTextChanged {
            if (number_tv.text.isNotBlank()) {
                order_bt.visibility = View.VISIBLE
            }else{
                order_bt.visibility = View.INVISIBLE
            }
        }
    }

    override fun setLayout() {
        setContentView(R.layout.activity_order_ware)
    }

    fun onSendClick(view: View){
        showBlockingLoading(true)
        viewModel.orderWare(ware.id!!, number_tv.text.toString().toDouble(), comments_tv.text.toString())
    }

    override fun setObservers() {
        viewModel.availability.observe(this) {
            ware.availabilities = it
            displayAvailability(false)
        }

        viewModel.orderResult.observe(this) {
            if (it) {
                val intent = Intent(this@OrderWareActivity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)

                Toast.makeText(this, "Przesłano zamówienie", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.result.observe(this){
            showBlockingLoading(false)
            okDialog("Błąd", getString(it), this)
        }
    }
}