package pl.tysia.maggstone.ui.wares

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_new_document.*
import kotlinx.android.synthetic.main.basic_catalog_layout.*
import pl.tysia.maggstone.data.Database
import pl.tysia.maggstone.data.model.Ware
import pl.tysia.maggstone.ui.RecyclerMarginDecorator
import pl.tysia.maggstone.ui.simple_list.SimpleListActivity
import pl.tysia.maggstone.ui.presentation_logic.adapter.ICatalogable
import pl.tysia.maggstone.ui.presentation_logic.filterer.StringFilter

class WareListActivity : SimpleListActivity() {
    override var filter : StringFilter<*> = StringFilter<Ware>(""){ filteredString, item ->
        item.title.toLowerCase().contains(filteredString.toLowerCase()) ||
        item.index!!.toLowerCase().contains(filteredString.toLowerCase())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


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


    override fun onItemSelected(item: ICatalogable?) {
        val returnIntent = Intent()
        returnIntent.putExtra(Ware.WARE_EXTRA, adapter.selectedItem as Ware)
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

}