package pl.tysia.maggstone.ui.scanner


import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.google.mlkit.vision.barcode.Barcode
import kotlinx.android.synthetic.main.activity_shelf_scanner.*
import me.dm7.barcodescanner.zxing.ZXingScannerView
import pl.tysia.maggstone.R
import pl.tysia.maggstone.data.NetAddressManager
import pl.tysia.maggstone.data.source.LoginDataSource
import pl.tysia.maggstone.data.source.LoginRepository
import pl.tysia.maggstone.okDialog
import pl.tysia.maggstone.ui.ViewModelFactory


class ShelfScannerActivity : ScanningActivity() {
    private lateinit var viewModel: ShelfScannerViewModel

    override fun setContentView() {
        setContentView(R.layout.activity_shelf_scanner)
    }

    override fun onSuccess(barcode: Barcode) {
        val token = LoginRepository(
            LoginDataSource(NetAddressManager(this@ShelfScannerActivity)),
            this@ShelfScannerActivity
        ).user!!.token

        qrCode = barcode.rawValue
        showSendingState(true)
        viewModel.testShelf(qrCode!!, token)

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