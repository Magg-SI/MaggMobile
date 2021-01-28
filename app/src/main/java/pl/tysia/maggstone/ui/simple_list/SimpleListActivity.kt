package pl.tysia.maggstone.ui.simple_list

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_new_document.*
import kotlinx.android.synthetic.main.basic_catalog_layout.*
import pl.tysia.maggstone.R
import pl.tysia.maggstone.ui.RecyclerMarginDecorator
import pl.tysia.maggstone.ui.login.afterTextChanged
import pl.tysia.maggstone.ui.presentation_logic.adapter.BasicCatalogAdapter
import pl.tysia.maggstone.ui.presentation_logic.adapter.CatalogAdapter
import pl.tysia.maggstone.ui.presentation_logic.adapter.ICatalogable
import pl.tysia.maggstone.ui.presentation_logic.filterer.StringFilter
import java.util.ArrayList


abstract class SimpleListActivity : AppCompatActivity(), CatalogAdapter.ItemSelectedListener<ICatalogable> {
    protected lateinit var adapter : BasicCatalogAdapter
    private lateinit var filter: StringFilter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.basic_catalog_layout)

        adapter = BasicCatalogAdapter(ArrayList())
        adapter.addItemSelectedListener(this)

        filter = StringFilter("")
        adapter.filterer.addFilter(filter)

        search_et.afterTextChanged {
            filter.filteredString = search_et.text.toString()
            adapter.filter()
            adapter.notifyDataSetChanged()
        }

        recyclerView.adapter = adapter

        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager

        recyclerView.addItemDecoration(RecyclerMarginDecorator(mTopFirst = 128, mBottomLast = 64))

    }

/*    override fun onItemSelected(item: ICatalogable?) {
        ok_button.isEnabled = true
    }*/

}

