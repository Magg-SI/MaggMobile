package pl.tysia.maggstone.ui.wares

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.room.Room
import pl.tysia.maggstone.constants.Extras
import pl.tysia.maggstone.constants.ListActivityMode
import pl.tysia.maggstone.data.Database
import pl.tysia.maggstone.data.model.Ware
import pl.tysia.maggstone.ui.order_ware.OrderWareActivity
import pl.tysia.maggstone.ui.simple_list.SimpleListActivity
import pl.tysia.maggstone.ui.presentation_logic.adapter.ICatalogable
import pl.tysia.maggstone.ui.presentation_logic.filterer.StringFilter

class WareListActivity : SimpleListActivity() {
    private lateinit var mode: String

    override var filter: StringFilter<ICatalogable> = StringFilter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        showBlockingLoading(true)
        mode = intent.getStringExtra(ListActivityMode.LIST_ACTIVITY_MODE_EXTRA)!!

        val db = Room.databaseBuilder(
            this,
            Database::class.java, "pl.tysia.database"
        ).build()

        db.waresDao().getAll().observe(this, Observer {
            adapter.allItems.clear()
            adapter.addAll(it as Collection<ICatalogable>?)
            adapter.filter()
            adapter.notifyDataSetChanged()
            showBlockingLoading(false)
        })
    }


    override fun onItemSelected(item: ICatalogable) {
        when (mode) {
            ListActivityMode.SELECT -> {
                val returnIntent = Intent()
                returnIntent.putExtra(Ware.WARE_EXTRA, adapter.selectedItem as Ware)
                setResult(Activity.RESULT_OK, returnIntent)
                finish()
            }
            ListActivityMode.BROWSE -> {
                val intent = Intent(this, WareInfoActivity::class.java)
                intent.putExtra(Ware.WARE_EXTRA, adapter.selectedItem as Ware)
                intent.putExtra(Extras.CALLING_ACTIVITY, this::class.java.simpleName)
                startActivity(intent)
            }
            ListActivityMode.ORDER -> {
                val intent = Intent(this, OrderWareActivity::class.java)
                intent.putExtra(Ware.WARE_EXTRA, adapter.selectedItem as Ware)
                intent.putExtra(Extras.CALLING_ACTIVITY, this::class.java.simpleName)
                startActivity(intent)
            }
        }

    }

}