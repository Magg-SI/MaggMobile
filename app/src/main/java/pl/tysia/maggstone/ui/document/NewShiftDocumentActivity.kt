package pl.tysia.maggstone.ui.document

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_new_document.*
import pl.tysia.maggstone.R
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
            startActivity(intent)

        }
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        adapter = ShiftDocumentAdapter(ArrayList())
        wares_recycler.adapter = adapter

        wareViewModel = ViewModelProvider(this,
            ViewModelFactory(this)
        ).get(WareInfoViewModel::class.java)

        wareViewModel.availability.observe(this@NewShiftDocumentActivity, Observer {
            lastWare!!.availabilities = it
            super.addWare(lastWare!!)
            showProgress(false)
        })

        wareViewModel.result.observe(this@NewShiftDocumentActivity, Observer {
            showProgress(false)
        })

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

    override fun addWare(ware: Ware) {
        lastWare = ware

        val token = LoginRepository(
            LoginDataSource(NetAddressManager(this)),
            this@NewShiftDocumentActivity
        ).user!!.token

        wareViewModel.getAvailabilities(ware.index!!, token)

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