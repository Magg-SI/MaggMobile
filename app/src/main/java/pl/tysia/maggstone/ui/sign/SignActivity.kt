package pl.tysia.maggstone.ui.sign

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_sign.*
import pl.tysia.maggstone.R
import pl.tysia.maggstone.constants.DocumentType
import pl.tysia.maggstone.constants.Extras
import pl.tysia.maggstone.data.NetAddressManager
import pl.tysia.maggstone.data.model.DocumentItem
import pl.tysia.maggstone.data.model.Warehouse
import pl.tysia.maggstone.data.source.LoginDataSource
import pl.tysia.maggstone.data.source.LoginRepository
import pl.tysia.maggstone.okDialog
import pl.tysia.maggstone.ui.ViewModelFactory
import pl.tysia.maggstone.ui.document.DocumentViewModel

class SignActivity : AppCompatActivity() {
    private lateinit var viewModel: DocumentViewModel
    private lateinit var items: ArrayList<DocumentItem>
    private lateinit var warehouse: Warehouse
    private lateinit var documentType: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign)

        val intent = Intent(this, SignActivity::class.java)
        warehouse = intent.getSerializableExtra(Extras.WAREHOUSE_EXTRA) as Warehouse
        documentType = intent.getStringExtra(Extras.DOCUMENT_TYPE)!!
        items = intent.getSerializableExtra(Extras.DOCUMENT_ITEMS_EXTRA) as ArrayList<DocumentItem>

        viewModel = ViewModelProvider(this,
            ViewModelFactory(this)
        ).get(DocumentViewModel::class.java)

        viewModel.documentsError.observe(this@SignActivity, Observer {
            okDialog("Błąd", it, this@SignActivity)
            showProgress(false)
        })

        viewModel.documentsResult.observe(this@SignActivity, Observer {
            Toast.makeText(this@SignActivity, it, Toast.LENGTH_SHORT).show()
            showProgress(false)
            finish()

        })
    }

    fun save(view : View){
        val token = LoginRepository(
            LoginDataSource(NetAddressManager(this)),
            this@SignActivity
        ).user!!.token

        when (documentType){
            DocumentType.SHIFT -> viewModel.sendShiftDocument(token, warehouse.id, items)
            DocumentType.OFFER -> viewModel.sendOfferDocument(token, warehouse.id, items)
        }

        viewModel.sendOfferDocument(token, warehouse.id, items)
    }

    private fun showProgress(show : Boolean){
        if (show){
            progress_bar.visibility = View.VISIBLE
            button.isEnabled = false
        }else {
            progress_bar.visibility = View.GONE
            button.isEnabled = true
        }
    }
}