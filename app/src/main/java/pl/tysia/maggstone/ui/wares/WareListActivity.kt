package pl.tysia.maggstone.ui.wares

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.room.Room
import kotlinx.android.synthetic.main.basic_catalog_layout.*
import pl.tysia.maggstone.data.Database
import pl.tysia.maggstone.data.model.Ware
import pl.tysia.maggstone.ui.SimpleListActivity
import pl.tysia.maggstone.ui.presentation_logic.adapter.ICatalogable

class WareListActivity : SimpleListActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ok_button.setOnClickListener {
            val returnIntent = Intent()
            returnIntent.putExtra(Ware.WARE_EXTRA, adapter.selectedItem as Ware)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }

        val db = Room.databaseBuilder(
            this,
            Database::class.java, "pl.tysia.database"
        ).build()

        db.waresDao().getAll().observe(this, Observer {
            adapter.allItems.clear()
            adapter.addAll(it as Collection<ICatalogable>?)
            adapter.filter()
            adapter.notifyDataSetChanged()
        })

    }
}