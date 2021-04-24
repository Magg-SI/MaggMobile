package pl.tysia.maggstone.ui.scanner

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.google.mlkit.vision.barcode.Barcode
import kotlinx.android.synthetic.main.activity_products_scanner.*
import pl.tysia.maggstone.R
import pl.tysia.maggstone.app.MaggApp
import pl.tysia.maggstone.okDialog
import javax.inject.Inject

class WaresShelfScannerActivity : ScanningActivity() {
    companion object{
        const val SHELF_EXTRA = "pl.tysia.maggstone.shelf_extra"
        const val BARCODE_PREFIX = "205"
        const val BARCODE_LENGTH = 9
    }

    override fun setContentView() {
        setContentView(R.layout.activity_products_scanner)

    }

    override fun onSuccess(barcode: Barcode) {
        val code = barcode.rawValue
        showSendingState(true)
        viewModel.changeLocation(code, shelf)
    }

    override fun isValid(barcode: Barcode): Boolean {
        return barcode.rawValue != null
                && barcode.rawValue.startsWith(BARCODE_PREFIX)
                && barcode.rawValue.length == BARCODE_LENGTH
                && (lastValue == null
                || lastValue!= null && lastValue!!.rawValue != barcode.rawValue)
    }

    private lateinit var shelf: String

    @Inject lateinit var viewModel : WaresShelfScannerViewModel


    public override fun onCreate(state: Bundle?) {
        super.onCreate(state)

        (application as MaggApp).appComponent.inject(this)

        shelf = intent.getStringExtra(SHELF_EXTRA)!!

        viewModel.locationError.observe(this, Observer {
            okDialog("Błąd", it, this)
            showSendingState(false)
        })

        viewModel.locationResult.observe(this, Observer {
            added_product_tv.text = it
            showSendingState(false)

        })
    }


    fun onFinishClicked(view: View?) {
        finish()
    }


}