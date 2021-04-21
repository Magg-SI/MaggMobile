package pl.tysia.maggstone.ui.contractors

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.room.Room
import kotlinx.android.synthetic.main.basic_catalog_layout.*
import pl.tysia.maggstone.constants.ListActivityMode
import pl.tysia.maggstone.data.Database
import pl.tysia.maggstone.data.model.Contractor
import pl.tysia.maggstone.ui.presentation_logic.adapter.ICatalogable
import pl.tysia.maggstone.ui.simple_list.SimpleListActivity

class ContractorListActivity : SimpleListActivity() {
    private lateinit var mode : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        showBlockingProgress(true)
        mode = intent.getStringExtra(ListActivityMode.LIST_ACTIVITY_MODE_EXTRA)!!

        val db = Room.databaseBuilder(
            this,
            Database::class.java, "pl.tysia.database"
        ).build()

        db.contractorsDao().getAll().observe(this, Observer {
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