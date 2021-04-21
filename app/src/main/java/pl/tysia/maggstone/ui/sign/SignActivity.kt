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
import pl.tysia.maggstone.data.model.Contractor
import pl.tysia.maggstone.data.model.DocumentItem
import pl.tysia.maggstone.data.model.Warehouse
import pl.tysia.maggstone.data.source.LoginDataSource
import pl.tysia.maggstone.data.source.LoginRepository
import pl.tysia.maggstone.getPhotoString
import pl.tysia.maggstone.okDialog
import pl.tysia.maggstone.ui.ViewModelFactory
import pl.tysia.maggstone.ui.document.DocumentViewModel
import pl.tysia.maggstone.ui.login.LoginActivity
import pl.tysia.maggstone.ui.main.MainActivity

class SignActivity : AppCompatActivity() {
    private lateinit var viewModel: DocumentViewModel
    private lateinit var items: ArrayList<DocumentItem>
    private lateinit var warehouse: Warehouse
    private lateinit var contractor: Contractor
    private lateinit var documentType: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign)

        documentType = intent.getStringExtra(Extras.DOCUMENT_TYPE)!!

        if (documentType == DocumentType.OFFER){
            contractor = intent.getSerializableExtra(Extras.CONTRACTOR_EXTRA) as Contractor
        }else if (documentType == DocumentType.SHIFT){
            warehouse = intent.getSerializableExtra(Extras.WAREHOUSE_EXTRA) as Warehouse
        }
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

            val intent = Intent(this@SignActivity, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)

        })
    }

    fun save(view : View){
        val token = LoginRepository(
            LoginDataSource(NetAddressManager(this)),
            this@SignActivity
        ).user!!.token

        val bitmap = sign_view.bmp
        val sign = getPhotoString(bitmap!!, 1f)

        val comments = comments_et.text.toString()

        when (documentType){
            DocumentType.SHIFT -> viewModel.sendShiftDocument(token, warehouse.id, sign,comments,items)
            DocumentType.OFFER -> viewModel.sendOfferDocument(token, contractor.id, sign, comments, items)
        }

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