package pl.tysia.maggstone.ui.document

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_new_document.*
import pl.tysia.maggstone.R
import pl.tysia.maggstone.data.model.Contractor
import pl.tysia.maggstone.data.model.DocumentItem
import pl.tysia.maggstone.data.model.Ware
import pl.tysia.maggstone.ui.contractors.ContractorListActivity
import pl.tysia.maggstone.ui.presentation_logic.RecyclerMarginDecorator
import pl.tysia.maggstone.ui.wares.WareListActivity
import pl.tysia.maggstone.ui.presentation_logic.adapter.CatalogAdapter
import pl.tysia.maggstone.ui.presentation_logic.adapter.DocumentAdapter
import pl.tysia.maggstone.ui.scanner.WareScannerActivity
import java.util.*

private const val WARE_REQUEST_CODE  = 1337
private const val CONTRACTOR_REQUEST_CODE  = 842

abstract class NewDocumentActivity : AppCompatActivity(), CatalogAdapter.EmptyListListener {
    private var contractor : Contractor? = null

    private lateinit var adapter : DocumentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = DocumentAdapter(ArrayList())

        wares_recycler.adapter = adapter
        wares_recycler.addItemDecoration(
            RecyclerMarginDecorator(
            mTopFirst = (resources.getDimension(R.dimen.document_top_bar).toInt())))

            val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        wares_recycler.layoutManager = linearLayoutManager

    }

    fun onSaveClick(view: View){

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

    override fun onListEmptied() {
        checkIfSaveAllowed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == WARE_REQUEST_CODE && resultCode == Activity.RESULT_OK ){
            val ware = data!!.getSerializableExtra(Ware.WARE_EXTRA) as Ware
            adapter.addItem(DocumentItem.WareItem(ware))
            adapter.filter()
            adapter.notifyDataSetChanged()
            checkIfSaveAllowed()
        } else if (requestCode == CONTRACTOR_REQUEST_CODE && resultCode == Activity.RESULT_OK ){
            contractor = data!!.getSerializableExtra(Contractor.CONTRACTOR_EXTRA) as Contractor
            contractor_tv.text = contractor!!.name
            checkIfSaveAllowed()
        }
    }

    fun onContractorSelectClick(view : View) {
        startActivityForResult(Intent(this, ContractorListActivity::class.java), CONTRACTOR_REQUEST_CODE)
    }

}