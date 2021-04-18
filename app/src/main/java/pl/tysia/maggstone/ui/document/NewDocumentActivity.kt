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
import pl.tysia.maggstone.R
import pl.tysia.maggstone.constants.ListActivityMode
import pl.tysia.maggstone.data.NetworkChangeReceiver
import pl.tysia.maggstone.data.model.DocumentItem
import pl.tysia.maggstone.data.model.Hose
import pl.tysia.maggstone.data.model.Ware
import pl.tysia.maggstone.okDialog
import pl.tysia.maggstone.ui.ViewModelFactory
import pl.tysia.maggstone.ui.hose.HoseActivity
import pl.tysia.maggstone.ui.wares.WareListActivity
import pl.tysia.maggstone.ui.presentation_logic.adapter.CatalogAdapter
import pl.tysia.maggstone.ui.presentation_logic.adapter.DocumentAdapter
import pl.tysia.maggstone.ui.scanner.WareScannerActivity


abstract class NewDocumentActivity : AppCompatActivity(), CatalogAdapter.EmptyListListener {
    protected lateinit var adapter : DocumentAdapter<DocumentItem>
    protected lateinit var viewModel: DocumentViewModel

    companion object{
        private const val WARE_REQUEST_CODE  = 1337
        private const val HOSE_REQUEST_CODE  = 1338
        const val CONTRACTOR_REQUEST_CODE  = 842
        const val WAREHOUSE_REQUEST_CODE  = 842
    }

    abstract fun save()
    abstract fun onSearch()
    abstract fun saveAllowed() : Boolean

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this,
            ViewModelFactory(this)
        ).get(DocumentViewModel::class.java)


        wares_recycler.addItemDecoration(
            pl.tysia.maggstone.ui.RecyclerMarginDecorator(
                mTopFirst = 16, mBottomLast = 128, mTop = 8, mBottom = 8
            )
        )

        adapter = DocumentAdapter(ArrayList())
        wares_recycler.adapter = adapter


        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        wares_recycler.layoutManager = linearLayoutManager

        viewModel.documentsError.observe(this@NewDocumentActivity, Observer {
            okDialog("Błąd", it, this@NewDocumentActivity)
            showProgress(false)
        })

        viewModel.documentsResult.observe(this@NewDocumentActivity, Observer {
            Toast.makeText(this@NewDocumentActivity, it, Toast.LENGTH_SHORT).show()
            showProgress(false)
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
        showProgress(true)
       save()
    }

    fun onScanClick(view: View){
        val intent = Intent(this, WareScannerActivity::class.java)
        intent.putExtra("theme", R.style.AppThemeRed)
        startActivityForResult(intent, WARE_REQUEST_CODE)
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
        } else if (requestCode == HOSE_REQUEST_CODE && resultCode == Activity.RESULT_OK ){
            val hose = data!!.getSerializableExtra(Hose.HOSE_EXTRA) as Hose
            adapter.addItem(DocumentItem(hose))
            adapter.filter()
            adapter.notifyDataSetChanged()
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