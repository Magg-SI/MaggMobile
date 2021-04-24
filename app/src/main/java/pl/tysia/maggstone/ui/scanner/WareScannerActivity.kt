package pl.tysia.maggstone.ui.scanner

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.google.mlkit.vision.barcode.Barcode
import pl.tysia.maggstone.R
import pl.tysia.maggstone.app.MaggApp
import pl.tysia.maggstone.data.model.Ware
import pl.tysia.maggstone.okDialog
import pl.tysia.maggstone.ui.wares.WareViewModel
import javax.inject.Inject


class WareScannerActivity : ScanningActivity() {
    companion object{
        const val BARCODE_PREFIX = "205"
        const val BARCODE_LENGTH = 9
    }

    @Inject lateinit var viewModel : WareViewModel

    override fun setContentView() {
        setContentView(R.layout.activity_scanning)
    }

    override fun onSuccess(barcode: Barcode) {
        val code = barcode.rawValue

        viewModel.getWare(code)
        showSendingState(true)
    }

    override fun isValid(barcode: Barcode): Boolean {
        return barcode.rawValue != null
                && barcode.rawValue.startsWith(BARCODE_PREFIX)
                && barcode.rawValue.length == BARCODE_LENGTH
                && (lastValue == null
                || lastValue!= null && lastValue!!.rawValue != barcode.rawValue)
    }

    override fun onCreate(state: Bundle?) {
        super.onCreate(state)

        (application as MaggApp).appComponent.inject(this)

        viewModel.ware.observe(this, Observer {
            val returnIntent = Intent()
            returnIntent.putExtra(Ware.WARE_EXTRA, viewModel.ware.value)
            setResult(Activity.RESULT_OK, returnIntent)
            showSendingState(false)
            finish()
        })

        viewModel.wareResult.observe(this, Observer {
            showSendingState(false)
            okDialog("Błąd", it, this)
        })
    }

}