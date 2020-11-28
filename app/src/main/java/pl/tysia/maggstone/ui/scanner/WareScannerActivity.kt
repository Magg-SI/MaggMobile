package pl.tysia.maggstone.ui.scanner

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.zxing.Result
import pl.tysia.maggstone.data.source.LoginDataSource
import pl.tysia.maggstone.data.source.LoginRepository
import pl.tysia.maggstone.data.model.Ware
import pl.tysia.maggstone.okDialog
import pl.tysia.maggstone.ui.wares.WareViewModel
import pl.tysia.maggstone.ui.wares.WareViewModelFactory

class WareScannerActivity : ScannerActivity() {
    private lateinit var viewModel : WareViewModel

    override fun onCreate(state: Bundle?) {
        super.onCreate(state)

        viewModel = ViewModelProvider(this, WareViewModelFactory())
        .get(WareViewModel::class.java)
    }

    override fun handleResult(rawResult: Result) {
        val code = rawResult.text

        viewModel.ware.observe(this, Observer {
            val returnIntent = Intent()
            returnIntent.putExtra(Ware.WARE_EXTRA, viewModel.ware.value)
            setResult(Activity.RESULT_OK, returnIntent)
            showSendingState(false)
            finish()
        })

        viewModel.wareResult.observe(this, Observer {
            val message = getString(viewModel.wareResult.value!!)
            showSendingState(false)
            okDialog("Błąd", message, this)
        })

        val token = LoginRepository(
            LoginDataSource(),
            this
        ).user!!.token

        viewModel.getWare(code, token)
        showSendingState(true)
    }
}