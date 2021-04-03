package pl.tysia.maggstone.ui.scanner


import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.google.mlkit.vision.barcode.Barcode
import kotlinx.android.synthetic.main.activity_shelf_scanner.*
import pl.tysia.maggstone.R
import pl.tysia.maggstone.data.NetAddressManager
import pl.tysia.maggstone.data.source.LoginDataSource
import pl.tysia.maggstone.data.source.LoginRepository
import pl.tysia.maggstone.okDialog
import pl.tysia.maggstone.ui.ViewModelFactory


class ShelfScannerActivity : ScanningActivity() {
    companion object{
        const val BARCODE_PREFIX = "206-"
        const val BARCODE_LENGTH = 8
    }

    private lateinit var viewModel: ShelfScannerViewModel

    override fun setContentView() {
        setContentView(R.layout.activity_shelf_scanner)
    }

    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun onSuccess(barcode: Barcode) {

            val token = LoginRepository(
                LoginDataSource(NetAddressManager(this@ShelfScannerActivity)),
                this@ShelfScannerActivity
            ).user!!.token

            qrCode = barcode.rawValue
            showSendingState(true)
            viewModel.testShelf(qrCode!!, token)

    }

    override fun isValid(barcode: Barcode): Boolean {
        return barcode.rawValue != null
                && barcode.rawValue.startsWith(BARCODE_PREFIX)
                && barcode.rawValue.length == BARCODE_LENGTH
    }

    private var qrCode : String? = null

    public override fun onCreate(state: Bundle?) {
        super.onCreate(state)

        viewModel = ViewModelProvider(this, ViewModelFactory(this))
            .get(ShelfScannerViewModel::class.java)

        viewModel.result.observe(this, Observer {
            startProductsScanning(qrCode!!)
        })

        viewModel.error.observe(this, Observer {
            okDialog(
                "Błąd",
                it,
                this
            )
            showSendingState(false)

        })

    }


    private fun startProductsScanning(code: String) {
        val returnIntent = Intent(this, WaresShelfScannerActivity::class.java)
        returnIntent.putExtra(WaresShelfScannerActivity.SHELF_EXTRA, code)
        startActivity(returnIntent)
        finish()
    }

}