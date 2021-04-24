package pl.tysia.maggstone.ui.contractors

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.room.Room
import kotlinx.android.synthetic.main.basic_catalog_layout.*
import pl.tysia.maggstone.app.MaggApp
import pl.tysia.maggstone.constants.ListActivityMode
import pl.tysia.maggstone.data.Database
import pl.tysia.maggstone.data.model.Contractor
import pl.tysia.maggstone.ui.presentation_logic.adapter.ICatalogable
import pl.tysia.maggstone.ui.simple_list.SimpleListActivity
import javax.inject.Inject

class ContractorListActivity : SimpleListActivity() {
    private lateinit var mode : String

    @Inject lateinit var db : Database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        showBlockingProgress(true)
        mode = intent.getStringExtra(ListActivityMode.LIST_ACTIVITY_MODE_EXTRA)!!

        (application as MaggApp).appComponent.inject(this)

        db.contractorsDao().getAll().observe(this, {
            adapter.allItems.clear()
            adapter.addAll(it as Collection<ICatalogable>?)
            adapter.filter()
            adapter.notifyDataSetChanged()
            showBlockingProgress(false)
        })

    }

    override fun onItemSelected(item: ICatalogable) {
        if (mode == ListActivityMode.SELECT) {
            val returnIntent = Intent()
            returnIntent.putExtra(Contractor.CONTRACTOR_EXTRA, adapter.selectedItem as Contractor)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }
    }
}