package pl.tysia.maggstone.ui.scanner

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.google.mlkit.vision.barcode.Barcode
import kotlinx.android.synthetic.main.activity_products_scanner.*
import pl.tysia.maggstone.R
import pl.tysia.maggstone.data.NetAddressManager
import pl.tysia.maggstone.data.source.LoginDataSource
import pl.tysia.maggstone.data.source.LoginRepository
import pl.tysia.maggstone.okDialog
import pl.tysia.maggstone.ui.ViewModelFactory
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
        val token = LoginRepository(
            LoginDataSource(NetAddressManager(this@WaresShelfScannerActivity)),
            this@WaresShelfScannerActivity
        ).user!!.token

        val code = barcode.rawValue
        showSendingState(true)
        viewModel.changeLocation(code, shelf, token)
    }

    override fun isValid(barcode: Barcode): Boolean {
        return barcode.rawValue != null
                && barcode.rawValue.startsWith(BARCODE_PREFIX)
                && barcode.rawValue.length == BARCODE_LENGTH
                && (lastValue == null
                || lastValue!= null && lastValue!!.rawValue != barcode.rawValue)
    }

    private lateinit var shelf: String

    private lateinit var viewModel : WaresShelfScannerViewModel


    public override fun onCreate(state: Bundle?) {
        super.onCreate(state)

        viewModel = ViewModelProvider(this, ViewModelFactory(this))
            .get(WaresShelfScannerViewModel::class.java)

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