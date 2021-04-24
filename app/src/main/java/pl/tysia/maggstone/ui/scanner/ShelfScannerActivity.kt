package pl.tysia.maggstone.ui.scanner


import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.google.mlkit.vision.barcode.Barcode
import pl.tysia.maggstone.R
import pl.tysia.maggstone.app.MaggApp
import pl.tysia.maggstone.okDialog
import javax.inject.Inject


class ShelfScannerActivity : ScanningActivity() {
    companion object{
        const val BARCODE_PREFIX = "206-"
        const val BARCODE_LENGTH = 8
    }

    @Inject lateinit var viewModel: ShelfScannerViewModel

    override fun setContentView() {
        setContentView(R.layout.activity_shelf_scanner)
    }

    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun onSuccess(barcode: Barcode) {

            qrCode = barcode.rawValue
            showSendingState(true)
            viewModel.testShelf(qrCode!!)

    }

    override fun isValid(barcode: Barcode): Boolean {
        return barcode.rawValue != null
                && barcode.rawValue.startsWith(BARCODE_PREFIX)
                && barcode.rawValue.length == BARCODE_LENGTH
    }

    private var qrCode : String? = null

    public override fun onCreate(state: Bundle?) {
        super.onCreate(state)

        (application as MaggApp).appComponent.inject(this)

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