package pl.tysia.maggstone.ui.ware_ordering

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_ware_packing.*
import pl.tysia.maggstone.R
import pl.tysia.maggstone.app.MaggApp
import pl.tysia.maggstone.data.model.OrderedWare
import pl.tysia.maggstone.data.model.Ware
import pl.tysia.maggstone.ui.BaseActivity
import javax.inject.Inject

class WareOrderingActivity : BaseActivity(), TextWatcher {
    private lateinit var orderedWare: OrderedWare

    @Inject lateinit var wareOrderingViewModel: WareOrderingViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ware_packing)

        (application as MaggApp).appComponent.inject(this)

        orderedWare = intent.getSerializableExtra(Ware.WARE_EXTRA) as OrderedWare

        wareOrderingViewModel.packResult.observe(this@WareOrderingActivity, Observer {
            val returnIntent = Intent()
            returnIntent.putExtra(Ware.WARE_EXTRA, orderedWare)
            setResult(Activity.RESULT_OK, returnIntent)
            showBlockingProgress(false)
            finish()
        })


        displayWare()

        next_et.addTextChangedListener(this)
        packed_et.addTextChangedListener(this)
        cancelled_et.addTextChangedListener(this)
    }

    private fun displayWare(){
        index_tv.text = orderedWare.index
        name_tv.text = orderedWare.name
        location_tv.text = orderedWare.location

        var orderedNumber = orderedWare.orderedNumber
        if (isInteger(orderedNumber)){
            ordered_tv.setText(orderedNumber.toInt().toString())
        }else{
            ordered_tv.setText(orderedNumber.toString())
        }

        var availability = orderedWare.availability!!
        if (isInteger(availability)){
            available_tv.setText(availability.toInt().toString())
        }else{
            available_tv.setText(availability.toString())
        }

        var cancelledNumber = orderedWare.cancelledNumber
        if (isInteger(cancelledNumber)){
            cancelled_et.setText(cancelledNumber.toInt().toString())
        }else{
            cancelled_et.setText(cancelledNumber.toString())
        }

        var packedNumber = orderedWare.packedNumber
        if (isInteger(packedNumber)){
            packed_et.setText(packedNumber.toInt().toString())
        }else{
            packed_et.setText(packedNumber.toString())
        }

        var postponedNumber = orderedWare.postponedNumber
        if (isInteger(postponedNumber)){
            next_et.setText(postponedNumber.toInt().toString())
        }else{
            next_et.setText(postponedNumber.toString())
        }
    }

    private fun isInteger(number: Double): Boolean {
        return number % 1 == 0.0
    }

    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        val cancelled = cancelled_et.text.let { if (it.isNullOrEmpty())  0.0 else it.toString().toDouble() }
        val next = next_et.text.let { if (it.isNullOrEmpty())  0.0 else it.toString().toDouble() }
        val packed = packed_et.text.let { if (it.isNullOrEmpty())  0.0 else it.toString().toDouble() }

        val sum = cancelled + next + packed
        total_tv.text = sum.toString()

        if (packed > orderedWare.availability!! || sum < orderedWare.orderedNumber!!){
            total_tv.setTextColor(Color.RED)
            save_button.isEnabled = false
        }else{
            total_tv.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
            save_button.isEnabled = true
        }
    }

    fun onPackAllClicked(view: View){
        orderedWare.packedNumber = orderedWare.orderedNumber
        orderedWare.cancelledNumber = 0.0
        orderedWare.postponedNumber = 0.0

        displayWare()

    }
    fun onOrderAllClicked(view: View){
        orderedWare.postponedNumber = orderedWare.orderedNumber
        orderedWare.cancelledNumber = 0.0
        orderedWare.packedNumber = 0.0

        displayWare()
    }

    fun onCancelAllClicked(view: View){
        orderedWare.cancelledNumber = orderedWare.orderedNumber
        orderedWare.packedNumber = 0.0
        orderedWare.postponedNumber = 0.0

        displayWare()
    }

    fun onFinishClicked(view: View){
        val ware = getOrderedWare()
        wareOrderingViewModel.packWare(ware.id!!, ware.packedNumber!!, ware.postponedNumber!!, ware.cancelledNumber!!)

        showBlockingProgress(true)
    }

    private fun getOrderedWare() : OrderedWare{
        val cancelled = cancelled_et.text.let { if (it.isNullOrEmpty())  0.0 else it.toString().toDouble() }
        val next = next_et.text.let { if (it.isNullOrEmpty())  0.0 else it.toString().toDouble() }
        val packed = packed_et.text.let { if (it.isNullOrEmpty())  0.0 else it.toString().toDouble() }

        orderedWare.postponedNumber = next
        orderedWare.packedNumber = packed
        orderedWare.cancelledNumber = cancelled

        return orderedWare
    }
}
