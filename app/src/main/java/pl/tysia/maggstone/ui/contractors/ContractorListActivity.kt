package pl.tysia.maggstone.ui.contractors

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.room.Room
import kotlinx.android.synthetic.main.basic_catalog_layout.*
import pl.tysia.maggstone.data.Database
import pl.tysia.maggstone.data.model.Contractor
import pl.tysia.maggstone.ui.presentation_logic.adapter.ICatalogable
import pl.tysia.maggstone.ui.SimpleListActivity

class ContractorListActivity : SimpleListActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ok_button.setOnClickListener {
            val returnIntent = Intent()
            returnIntent.putExtra(Contractor.CONTRACTOR_EXTRA, adapter.selectedItem as Contractor)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }

        val db = Room.databaseBuilder(
            this,
            Database::class.java, "pl.tysia.database"
        ).build()

        db.contractorsDao().getAll().observe(this, Observer {
            adapter.allItems.clear()
            adapter.addAll(it as Collection<ICatalogable>?)
            adapter.filter()
            adapter.notifyDataSetChanged()
        })

    }
}