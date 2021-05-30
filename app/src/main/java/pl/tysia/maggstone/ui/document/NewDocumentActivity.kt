package pl.tysia.maggstone.ui.document

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_new_document.*
import pl.tysia.maggstone.R
import pl.tysia.maggstone.app.MaggApp
import pl.tysia.maggstone.constants.ListActivityMode
import pl.tysia.maggstone.data.NetworkChangeReceiver
import pl.tysia.maggstone.data.model.*
import pl.tysia.maggstone.okDialog
import pl.tysia.maggstone.ui.BaseActivity
import pl.tysia.maggstone.ui.hose.HoseActivity
import pl.tysia.maggstone.ui.wares.WareListActivity
import pl.tysia.maggstone.ui.presentation_logic.adapter.CatalogAdapter
import pl.tysia.maggstone.ui.presentation_logic.adapter.DocumentAdapter
import pl.tysia.maggstone.ui.scanner.WareScannerActivity
import pl.tysia.maggstone.ui.service.ServiceActivity
import javax.inject.Inject


abstract class NewDocumentActivity : BaseActivity(), CatalogAdapter.ListChangeListener {
    protected lateinit var adapter : DocumentAdapter<DocumentItem>
    protected var contractor : Contractor? = null
    @Inject lateinit var viewModel: DocumentViewModel

    companion object{
        const val WARE_REQUEST_CODE  = 1337
        const val HOSE_REQUEST_CODE  = 1338
        const val SERVICE_REQUEST_CODE  = 1339
        const val CONTRACTOR_REQUEST_CODE  = 842
        const val WAREHOUSE_REQUEST_CODE  = 843
    }

    abstract fun save()
    abstract fun onSearch()
    abstract fun saveAllowed() : Boolean
    abstract fun getDocumentAdapter() : DocumentAdapter<DocumentItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as MaggApp).appComponent.inject(this)

        wares_recycler.addItemDecoration(
            pl.tysia.maggstone.ui.RecyclerMarginDecorator(
                mTopFirst = 16, mBottomLast = 128, mTop = 8, mBottom = 8
            )
        )

        adapter = getDocumentAdapter()
        wares_recycler.adapter = adapter

        adapter.addChangeListener(this)

        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        wares_recycler.layoutManager = linearLayoutManager

        viewModel.documentsError.observe(this@NewDocumentActivity, Observer {
            okDialog("Błąd", it, this@NewDocumentActivity)
            showBlockingLoading(false)
        })

        viewModel.documentsResult.observe(this@NewDocumentActivity, Observer {
            Toast.makeText(this@NewDocumentActivity, it, Toast.LENGTH_SHORT).show()
            showBlockingLoading(false)
            finish()

        })


    }

    fun onSaveClick(view: View){
        save()
    }

    fun onScanClick(view: View){
        val intent = Intent(this, WareScannerActivity::class.java)
        intent.putExtra("theme", R.style.AppThemeYellow)
        startActivityForResult(intent, WARE_REQUEST_CODE)
    }

    fun onServiceClick(view: View){
        val intent = Intent(this, ServiceActivity::class.java)
        if(contractor != null) intent.putExtra("ktrID", contractor!!.id)
        startActivityForResult(intent, SERVICE_REQUEST_CODE)
    }

    fun onWareFromListClick(view: View){
        val intent = Intent(this, WareListActivity::class.java)
        intent.putExtra(ListActivityMode.LIST_ACTIVITY_MODE_EXTRA, ListActivityMode.SELECT)
        startActivityForResult(intent, WARE_REQUEST_CODE)
    }

    protected fun checkIfSaveAllowed(){
        save_button.isEnabled = saveAllowed()
    }

    fun onHoseClicked(view: View) {
        if (NetworkChangeReceiver.internetConnection.value!!) {
            val intent = Intent(this, HoseActivity::class.java)
            if(contractor != null) intent.putExtra("ktrID", contractor!!.id)
            startActivityForResult(intent, HOSE_REQUEST_CODE)
        }
        else okDialog("Brak połączenia z internetem", "Żeby utworzyć wąż konieczne jest połączenie z internetem.", this)
    }

    override fun onListChanged() {
        checkIfSaveAllowed()
    }

    open fun addWare(ware : Ware){
        adapter.addItem(DocumentItem(ware))
        adapter.filter()
        adapter.notifyDataSetChanged()
        setSums()
        checkIfSaveAllowed()
    }

    open fun addHose(hose : Hose){
        adapter.addItem(DocumentItem(hose))
        adapter.filter()
        adapter.notifyDataSetChanged()
        setSums()
        checkIfSaveAllowed()
    }

    open fun addService(service : Service){
        adapter.addItem(DocumentItem(service))
        adapter.filter()
        adapter.notifyDataSetChanged()
        setSums()
        checkIfSaveAllowed()
    }

    open fun setSums(){
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == WARE_REQUEST_CODE && resultCode == Activity.RESULT_OK ){
            val ware = data!!.getSerializableExtra(Ware.WARE_EXTRA) as Ware
            addWare(ware)
        } else if (requestCode == HOSE_REQUEST_CODE && resultCode == Activity.RESULT_OK ){
            val hose = data!!.getSerializableExtra(Hose.HOSE_EXTRA) as Hose
            addHose(hose)
        } else if (requestCode == SERVICE_REQUEST_CODE && resultCode == Activity.RESULT_OK ){
            val service = data!!.getSerializableExtra(Service.SERVICE_EXTRA) as Service
            addService(service)
        }

        checkIfSaveAllowed()
    }

    fun onContractorSelectClick(view : View) {
        onSearch()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putSerializable("items", adapter.allItems)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        adapter.addAll(savedInstanceState.get("items") as List<DocumentItem>)
        adapter.filter()
        adapter.notifyDataSetChanged()
    }

}