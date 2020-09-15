package pl.tysia.maggstone.ui.orders

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_ware_ordering.*
import pl.tysia.maggstone.R
import pl.tysia.maggstone.data.model.DocumentItem
import pl.tysia.maggstone.data.model.Ware
import java.io.IOException

class WareOrderingActivity : AppCompatActivity(), TextWatcher {
    private lateinit var orderedWare: DocumentItem.OrderedWareItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ware_ordering)

        orderedWare = intent.getSerializableExtra(Ware.WARE_EXTRA) as DocumentItem.OrderedWareItem

        displayWare()

        next_et.addTextChangedListener(this)
        packed_et.addTextChangedListener(this)
        cancelled_et.addTextChangedListener(this)
    }

    private fun displayWare(){
        (orderedWare.item as Ware).also {
            index_tv.text = it.index
            name_tv.text = it.name
            location_tv.text = it.location
        }


        ordered_tv.text = orderedWare.orderedNumber.toString()
        available_tv.text = orderedWare.availability.toString()

        cancelled_et.setText(orderedWare.cancelledNumber.toString())
        packed_et.setText(orderedWare.packedNumber.toString())
        next_et.setText(orderedWare.postponedNumber.toString())
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

        if (sum > orderedWare.availability || sum < orderedWare.orderedNumber){
            total_tv.setTextColor(Color.RED)
            save_button.isEnabled = false
        }else{
            total_tv.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
            save_button.isEnabled = true
        }
    }

    public fun onPackAllClicked(view: View){
        packed_et.setText(orderedWare.orderedNumber.toString())
        cancelled_et.setText(0.toString())
        next_et.setText(0.toString())

    }
    public fun onOrderAllClicked(view: View){
        next_et.setText(orderedWare.orderedNumber.toString())
        packed_et.setText(0.toString())
        cancelled_et.setText(0.toString())

    }
    public fun onCancelAllClicked(view: View){
        cancelled_et.setText(orderedWare.orderedNumber.toString())
        packed_et.setText(0.toString())
        next_et.setText(0.toString())

    }

    private fun getOrderedWare() : DocumentItem.OrderedWareItem{
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
