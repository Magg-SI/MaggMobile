package pl.tysia.maggstone.ui.sign

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_sign.*
import pl.tysia.maggstone.R
import pl.tysia.maggstone.app.MaggApp
import pl.tysia.maggstone.constants.DocumentType
import pl.tysia.maggstone.constants.Extras
import pl.tysia.maggstone.data.model.Contractor
import pl.tysia.maggstone.data.model.DocumentItem
import pl.tysia.maggstone.data.model.Warehouse
import pl.tysia.maggstone.getPhotoString
import pl.tysia.maggstone.okDialog
import pl.tysia.maggstone.ui.BaseActivity
import pl.tysia.maggstone.ui.document.DocumentViewModel
import pl.tysia.maggstone.ui.main.MainActivity
import javax.inject.Inject

class SignActivity : BaseActivity() {
    @Inject lateinit var viewModel: DocumentViewModel

    private lateinit var items: ArrayList<DocumentItem>
    private lateinit var warehouse: Warehouse
    private lateinit var contractor: Contractor
    private lateinit var documentType: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign)

        (application as MaggApp).appComponent.inject(this)

        documentType = intent.getStringExtra(Extras.DOCUMENT_TYPE)!!

        if (documentType == DocumentType.OFFER){
            contractor = intent.getSerializableExtra(Extras.CONTRACTOR_EXTRA) as Contractor
        }else if (documentType == DocumentType.SHIFT){
            warehouse = intent.getSerializableExtra(Extras.WAREHOUSE_EXTRA) as Warehouse
        }
        items = intent.getSerializableExtra(Extras.DOCUMENT_ITEMS_EXTRA) as ArrayList<DocumentItem>

        viewModel.documentsError.observe(this@SignActivity, Observer {
            okDialog("Błąd", it, this@SignActivity)
            showBlockingProgress(false)
        })

        viewModel.documentsResult.observe(this@SignActivity, Observer {
            Toast.makeText(this@SignActivity, it, Toast.LENGTH_SHORT).show()
            showBlockingProgress(false)
            val intent = Intent(this@SignActivity, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)

        })
    }

    fun save(view : View){
        showBlockingProgress(true)

        val bitmap = sign_view.bmp
        val sign = getPhotoString(bitmap!!, 1f)

        val comments = comments_et.text.toString()

        when (documentType){
            DocumentType.SHIFT -> viewModel.sendShiftDocument(warehouse.id, sign,comments,items)
            DocumentType.OFFER -> viewModel.sendOfferDocument(contractor.id, sign, comments, items)
        }

    }
}