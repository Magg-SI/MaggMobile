package pl.tysia.maggstone.ui.document

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_new_document.*
import pl.tysia.maggstone.R
import pl.tysia.maggstone.constants.ListActivityMode
import pl.tysia.maggstone.data.NetAddressManager
import pl.tysia.maggstone.data.model.Warehouse
import pl.tysia.maggstone.data.source.LoginDataSource
import pl.tysia.maggstone.data.source.LoginRepository
import pl.tysia.maggstone.ui.contractors.ContractorListActivity
import pl.tysia.maggstone.ui.warehouses.WarehousesListActivity

class NewShiftDocumentActivity : NewDocumentActivity() {
    protected var warehouse : Warehouse? = null

    override fun save() {
        if(warehouse != null && adapter.allItems.isNotEmpty()) {
            val token = LoginRepository(
                LoginDataSource(NetAddressManager(this)),
                this@NewShiftDocumentActivity
            ).user!!.token

            viewModel.sendShiftDocument(token, warehouse!!.id, adapter.allItems)
        }
    }

    override fun onSearch() {
        val intent = Intent(this, WarehousesListActivity::class.java)
        intent.putExtra(ListActivityMode.LIST_ACTIVITY_MODE_EXTRA, ListActivityMode.SELECT)
        startActivityForResult(intent, WAREHOUSE_REQUEST_CODE)
    }

    override fun saveAllowed(): Boolean {
        return warehouse != null && adapter.allItems.isNotEmpty()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_new_shift_document)

        super.onCreate(savedInstanceState)
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