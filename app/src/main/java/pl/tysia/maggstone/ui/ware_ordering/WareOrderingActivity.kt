package pl.tysia.maggstone.ui.ware_ordering

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_ware_ordering.*
import pl.tysia.maggstone.R
import pl.tysia.maggstone.data.NetAddressManager
import pl.tysia.maggstone.data.model.OrderedWare
import pl.tysia.maggstone.data.model.Ware
import pl.tysia.maggstone.data.source.LoginDataSource
import pl.tysia.maggstone.data.source.LoginRepository
import pl.tysia.maggstone.ui.ViewModelFactory

class WareOrderingActivity : AppCompatActivity(), TextWatcher {
    private lateinit var orderedWare: OrderedWare

    private lateinit var wareOrderingViewModel: WareOrderingViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ware_packing)

        orderedWare = intent.getSerializableExtra(Ware.WARE_EXTRA) as OrderedWare

        wareOrderingViewModel = ViewModelProvider(this,
            ViewModelFactory(this)
        ).get(WareOrderingViewModel::class.java)


        wareOrderingViewModel.packResult.observe(this@WareOrderingActivity, Observer {
            val returnIntent = Intent()
            returnIntent.putExtra(Ware.WARE_EXTRA, orderedWare)
            setResult(Activity.RESULT_OK, returnIntent)
            showProgress(false)
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
        val token = LoginRepository(
            LoginDataSource(NetAddressManager(this)),
            this
        ).user!!.token

        val ware = getOrderedWare()
        wareOrderingViewModel.packWare(token,ware.id!!, ware.packedNumber!!, ware.postponedNumber!!, ware.cancelledNumber!!)

        showProgress(true)
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



    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private fun showProgress(show: Boolean) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

        form.visibility = if (show) View.GONE else View.VISIBLE
        form.animate()
            .setDuration(shortAnimTime)
            .alpha((if (show) 0 else 1).toFloat())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    form.visibility = if (show) View.GONE else View.VISIBLE
                }
            })

        progressBar.visibility = if (show) View.VISIBLE else View.GONE
        progressBar.animate()
            .setDuration(shortAnimTime)
            .alpha((if (show) 1 else 0).toFloat())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    progressBar.visibility = if (show) View.VISIBLE else View.GONE
                }
            })
    }
}
