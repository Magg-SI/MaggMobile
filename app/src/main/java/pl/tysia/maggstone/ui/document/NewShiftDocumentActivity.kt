package pl.tysia.maggstone.ui.document

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_new_document.*
import pl.tysia.maggstone.R
import pl.tysia.maggstone.constants.Extras.DOCUMENT_ITEMS_EXTRA
import pl.tysia.maggstone.constants.Extras.WAREHOUSE_EXTRA
import pl.tysia.maggstone.constants.ListActivityMode
import pl.tysia.maggstone.data.NetAddressManager
import pl.tysia.maggstone.data.model.DocumentItem
import pl.tysia.maggstone.data.model.Ware
import pl.tysia.maggstone.data.model.Warehouse
import pl.tysia.maggstone.data.source.LoginDataSource
import pl.tysia.maggstone.data.source.LoginRepository
import pl.tysia.maggstone.okDialog
import pl.tysia.maggstone.ui.ViewModelFactory
import pl.tysia.maggstone.ui.contractors.ContractorListActivity
import pl.tysia.maggstone.ui.sign.SignActivity
import pl.tysia.maggstone.ui.presentation_logic.adapter.DocumentAdapter
import pl.tysia.maggstone.ui.presentation_logic.adapter.ShiftDocumentAdapter
import pl.tysia.maggstone.ui.warehouses.WarehousesListActivity
import pl.tysia.maggstone.ui.wares.WareInfoViewModel
import pl.tysia.maggstone.ui.wares.WareViewModel

class NewShiftDocumentActivity : NewDocumentActivity() {
    protected var warehouse : Warehouse? = null

    private lateinit var wareViewModel : WareInfoViewModel
    private var lastWare : Ware? = null

    override fun save() {
        if(warehouse != null && adapter.allItems.isNotEmpty()) {

            val intent = Intent(this, SignActivity::class.java)
            intent.putExtra(WAREHOUSE_EXTRA, warehouse)
            intent.putExtra(DOCUMENT_ITEMS_EXTRA, adapter.allItems)
            startActivity(intent)

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_new_shift_document)

        super.onCreate(savedInstanceState)

        wareViewModel = ViewModelProvider(this,
            ViewModelFactory(this)
        ).get(WareInfoViewModel::class.java)

        wareViewModel!!.availability.observe(this@NewShiftDocumentActivity, Observer {
            lastWare!!.availabilities = it

            if (lastWare!!.availabilities!![0].quantity <= 0 ){
                Toast.makeText(this@NewShiftDocumentActivity, "Brak produktu w magazynie", Toast.LENGTH_LONG).show()
            }else{
                super.addWare(lastWare!!)
            }

            showBlockingProgress(false)
        })

        wareViewModel!!.result.observe(this@NewShiftDocumentActivity, Observer {
            Toast.makeText(this@NewShiftDocumentActivity, it, Toast.LENGTH_LONG).show()
            showBlockingProgress(false)
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
        showBlockingProgress(true)

        val token = LoginRepository(
            LoginDataSource(NetAddressManager(this)),
            this@NewShiftDocumentActivity
        ).user!!.token

        wareViewModel!!.getAvailabilities(ware.index!!, token)

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