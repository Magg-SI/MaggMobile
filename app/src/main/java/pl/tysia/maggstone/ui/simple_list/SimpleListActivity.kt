package pl.tysia.maggstone.ui.simple_list

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.basic_catalog_layout.*
import pl.tysia.maggstone.R
import pl.tysia.maggstone.ui.BaseActivity
import pl.tysia.maggstone.ui.RecyclerMarginDecorator
import pl.tysia.maggstone.ui.login.afterTextChanged
import pl.tysia.maggstone.ui.presentation_logic.adapter.BasicCatalogAdapter
import pl.tysia.maggstone.ui.presentation_logic.adapter.CatalogAdapter
import pl.tysia.maggstone.ui.presentation_logic.adapter.ICatalogable
import pl.tysia.maggstone.ui.presentation_logic.filterer.StringFilter
import java.util.ArrayList


abstract class SimpleListActivity : BaseActivity(), CatalogAdapter.ItemSelectedListener<ICatalogable> {
    protected lateinit var adapter : BasicCatalogAdapter
    protected open var filter: StringFilter<ICatalogable> = StringFilter<ICatalogable>(null){ filteredStrings, item ->
        filteredStrings.count { item.getFilteredValue().toLowerCase().contains(it.toLowerCase()) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.basic_catalog_layout)


        adapter = BasicCatalogAdapter(ArrayList())

        adapter.addItemSelectedListener(this)

        adapter.filterer.addFilter(filter)

        search_et.afterTextChanged {
            filter.filteredStrings = search_et.text.toString().split(" ")
            adapter.filter()
            adapter.notifyDataSetChanged()
        }

        recyclerView.adapter = adapter

        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager

        recyclerView.addItemDecoration(RecyclerMarginDecorator(mTopFirst = 200, mBottomLast = 64))

    }

}

