package pl.tysia.maggstone.ui.document

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_new_document.*
import pl.tysia.maggstone.R
import pl.tysia.maggstone.constants.ListActivityMode
import pl.tysia.maggstone.data.NetAddressManager
import pl.tysia.maggstone.data.model.Contractor
import pl.tysia.maggstone.data.source.LoginDataSource
import pl.tysia.maggstone.data.source.LoginRepository
import pl.tysia.maggstone.ui.contractors.ContractorListActivity
import pl.tysia.maggstone.ui.presentation_logic.adapter.DocumentAdapter

class BasicNewDocumentActivity : NewDocumentActivity() {
    protected var contractor : Contractor? = null

    override fun save() {
        if(contractor != null && adapter.allItems.isNotEmpty()) {
            val token = LoginRepository(
                LoginDataSource(NetAddressManager(this)),
                this@BasicNewDocumentActivity
            ).user!!.token

            viewModel.sendOfferDocument(token, contractor!!.id, adapter.allItems)
        }
    }

    override fun onSearch() {
        val intent = Intent(this, ContractorListActivity::class.java)
        intent.putExtra(ListActivityMode.LIST_ACTIVITY_MODE_EXTRA, ListActivityMode.SELECT)
        startActivityForResult(intent, CONTRACTOR_REQUEST_CODE)
    }

    override fun saveAllowed(): Boolean {
        val badItems = adapter.allItems.filter { item -> item.iloscOk!=0 }

        return contractor != null && adapter.allItems.isNotEmpty() && badItems.isEmpty()
    }

    override fun getDocumentAdapter(): DocumentAdapter<DocumentItem> {
        return DocumentAdapter(ArrayList())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_new_document)

        super.onCreate(savedInstanceState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

      if (requestCode == CONTRACTOR_REQUEST_CODE && resultCode == Activity.RESULT_OK ){
            contractor = data!!.getSerializableExtra(Contractor.CONTRACTOR_EXTRA) as Contractor
            contractor_tv.text = contractor!!.name
        }

        checkIfSaveAllowed()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putSerializable("contractor", contractor)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        val contractor = savedInstanceState.get("contractor")
        if (contractor != null){
            this.contractor = contractor as Contractor
            contractor_tv.text = this.contractor!!.name
        }
    }
}