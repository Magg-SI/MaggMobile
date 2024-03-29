package pl.tysia.maggstone.ui.document

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_new_document.*
import pl.tysia.maggstone.R
import pl.tysia.maggstone.app.MaggApp
import pl.tysia.maggstone.constants.DocumentType
import pl.tysia.maggstone.constants.Extras
import pl.tysia.maggstone.constants.ListActivityMode
import pl.tysia.maggstone.data.model.DocumentItem
import pl.tysia.maggstone.data.model.Ware
import pl.tysia.maggstone.data.model.Warehouse
import pl.tysia.maggstone.ui.sign.SignActivity
import pl.tysia.maggstone.ui.presentation_logic.adapter.DocumentAdapter
import pl.tysia.maggstone.ui.presentation_logic.adapter.ShiftDocumentAdapter
import pl.tysia.maggstone.ui.warehouses.WarehousesListActivity
import pl.tysia.maggstone.ui.wares.WareInfoViewModel
import javax.inject.Inject

class NewShiftDocumentActivity : NewDocumentActivity() {
    protected var warehouse : Warehouse? = null

    @Inject lateinit var wareViewModel : WareInfoViewModel
    private var lastWare : Ware? = null

    override fun save() {
        if(warehouse != null && adapter.allItems.isNotEmpty()) {

            val intent = Intent(this, SignActivity::class.java)
            intent.putExtra(Extras.WAREHOUSE_EXTRA, warehouse)
            intent.putExtra(Extras.DOCUMENT_ITEMS_EXTRA, adapter.allItems)
            intent.putExtra(Extras.DOCUMENT_TYPE, DocumentType.SHIFT)
            startActivity(intent)

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_new_shift_document)

        super.onCreate(savedInstanceState)

        (application as MaggApp).appComponent.inject(this)


        wareViewModel!!.availability.observe(this@NewShiftDocumentActivity, Observer {
            lastWare!!.availabilities = it

            if (lastWare!!.availabilities!![0].quantity <= 0 ){
                Toast.makeText(this@NewShiftDocumentActivity, "Brak produktu w magazynie", Toast.LENGTH_LONG).show()
            }else{
                super.addWare(lastWare!!)
            }

            showBlockingLoading(false)
        })

        wareViewModel!!.result.observe(this@NewShiftDocumentActivity, Observer {
            Toast.makeText(this@NewShiftDocumentActivity, it, Toast.LENGTH_LONG).show()
            showBlockingLoading(false)
        })

    }

    override fun onSearch() {
        val intent = Intent(this, WarehousesListActivity::class.java)
        intent.putExtra(ListActivityMode.LIST_ACTIVITY_MODE_EXTRA, ListActivityMode.SELECT)
        startActivityForResult(intent, WAREHOUSE_REQUEST_CODE)
    }

    override fun saveAllowed(): Boolean {
        val badItems = adapter.allItems.filter { item -> item.iloscOk!=0 }
        return warehouse != null && adapter.allItems.isNotEmpty() && badItems.isEmpty()
    }

    override fun getDocumentAdapter(): DocumentAdapter<DocumentItem> {
        return ShiftDocumentAdapter(ArrayList())
    }

    override fun addWare(ware: Ware) {
        lastWare = ware
        showBlockingLoading(true)

        wareViewModel!!.getAvailabilities(ware.index!!)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == WAREHOUSE_REQUEST_CODE && resultCode == Activity.RESULT_OK ){
            warehouse = data!!.getSerializableExtra(Warehouse.WAREHOUSE_EXTRA) as Warehouse
            contractor_tv.text = warehouse!!.name
        }

        checkIfSaveAllowed()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putSerializable("warehouse", warehouse)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        val warehouse = savedInstanceState.get("warehouse")
        if (warehouse != null){
            this.warehouse = warehouse as Warehouse
            contractor_tv.text = this.warehouse!!.name
        }
    }

}