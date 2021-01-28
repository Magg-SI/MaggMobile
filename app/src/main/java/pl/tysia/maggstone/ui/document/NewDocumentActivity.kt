package pl.tysia.maggstone.ui.document

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_new_document.*
import pl.tysia.maggstone.data.NetAddressManager
import pl.tysia.maggstone.data.NetworkChangeReceiver
import pl.tysia.maggstone.data.model.Contractor
import pl.tysia.maggstone.data.model.DocumentItem
import pl.tysia.maggstone.data.model.Hose
import pl.tysia.maggstone.data.model.Ware
import pl.tysia.maggstone.data.source.LoginDataSource
import pl.tysia.maggstone.data.source.LoginRepository
import pl.tysia.maggstone.okDialog
import pl.tysia.maggstone.ui.ViewModelFactory
import pl.tysia.maggstone.ui.contractors.ContractorListActivity
import pl.tysia.maggstone.ui.hose.HoseActivity
import pl.tysia.maggstone.ui.wares.WareListActivity
import pl.tysia.maggstone.ui.presentation_logic.adapter.CatalogAdapter
import pl.tysia.maggstone.ui.presentation_logic.adapter.DocumentAdapter
import pl.tysia.maggstone.ui.scanner.WareScannerActivity


private const val WARE_REQUEST_CODE  = 1337
private const val HOSE_REQUEST_CODE  = 1338
private const val CONTRACTOR_REQUEST_CODE  = 842

abstract class NewDocumentActivity : AppCompatActivity(), CatalogAdapter.EmptyListListener {
    private var contractor : Contractor? = null
    private lateinit var adapter : DocumentAdapter<DocumentItem>
    private lateinit var viewModel: DocumentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this,
            ViewModelFactory(this)
        ).get(DocumentViewModel::class.java)


        wares_recycler.addItemDecoration(
            pl.tysia.maggstone.ui.RecyclerMarginDecorator(
                mTopFirst = 16, mBottomLast = 128
            )
        )

        adapter = DocumentAdapter(ArrayList())
        wares_recycler.adapter = adapter


        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        wares_recycler.layoutManager = linearLayoutManager

        viewModel.documentsError.observe(this@NewDocumentActivity, Observer {
            okDialog("Błąd", it, this@NewDocumentActivity)
        })

        viewModel.documentsResult.observe(this@NewDocumentActivity, Observer {
            Toast.makeText(this@NewDocumentActivity, it, Toast.LENGTH_SHORT).show()
            finish()

        })


    }

    private fun showProgress(show : Boolean){
        if (show){
            progressBar3.visibility = View.VISIBLE

        }else {
            progressBar3.visibility = View.GONE

        }

        save_button.isEnabled = !show
    }

    fun onSaveClick(view: View){
        val token = LoginRepository(
            LoginDataSource(NetAddressManager(this)),
            this@NewDocumentActivity
        ).user!!.token

        viewModel.sendDocument(token, contractor!!.id, adapter.allItems)
    }

    fun onScanClick(view: View){
        startActivityForResult(Intent(this, WareScannerActivity::class.java), WARE_REQUEST_CODE)
    }

    fun onWareFromListClick(view: View){
        startActivityForResult(Intent(this, WareListActivity::class.java), WARE_REQUEST_CODE)
    }

    private fun checkIfSaveAllowed(){
        save_button.isEnabled = contractor != null && adapter.allItems.isNotEmpty()
    }

    fun onHoseClicked(view: View) {
        if (NetworkChangeReceiver.internetConnection.value!!)
            startActivityForResult(Intent(this, HoseActivity::class.java), HOSE_REQUEST_CODE)
        else okDialog("Brak połączenia z internetem", "Żeby utworzyć wąż konieczne jest połączenie z internetem.", this)
    }

    override fun onListEmptied() {
        checkIfSaveAllowed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == WARE_REQUEST_CODE && resultCode == Activity.RESULT_OK ){
            val ware = data!!.getSerializableExtra(Ware.WARE_EXTRA) as Ware
            adapter.addItem(DocumentItem(ware))
            adapter.filter()
            adapter.notifyDataSetChanged()
        } else if (requestCode == CONTRACTOR_REQUEST_CODE && resultCode == Activity.RESULT_OK ){
            contractor = data!!.getSerializableExtra(Contractor.CONTRACTOR_EXTRA) as Contractor
            contractor_tv.text = contractor!!.name
        } else if (requestCode == HOSE_REQUEST_CODE && resultCode == Activity.RESULT_OK ){
            val hose = data!!.getSerializableExtra(Hose.HOSE_EXTRA) as Hose
            adapter.addItem(DocumentItem(hose))
            adapter.filter()
            adapter.notifyDataSetChanged()
        }

        checkIfSaveAllowed()
    }

    fun onContractorSelectClick(view : View) {
        startActivityForResult(Intent(this, ContractorListActivity::class.java), CONTRACTOR_REQUEST_CODE)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putSerializable("items", adapter.allItems)
        outState.putSerializable("contractor", contractor)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        adapter.addAll(savedInstanceState.get("items") as List<DocumentItem>)
        adapter.filter()
        adapter.notifyDataSetChanged()

        val contractor = savedInstanceState.get("contractor")
        if (contractor != null){
            this.contractor = contractor as Contractor
            contractor_tv.text = this.contractor!!.name
        }
    }

}