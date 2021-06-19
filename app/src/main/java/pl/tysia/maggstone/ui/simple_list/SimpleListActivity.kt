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
    protected open var filter: StringFilter<ICatalogable> = StringFilter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setLayout()

        adapter = BasicCatalogAdapter(ArrayList())

        adapter.addItemSelectedListener(this)

        adapter.filterer.addFilter(filter)

        search_et.afterTextChanged {
            filter.filteredString = search_et.text.toString()
            adapter.filter()
            adapter.notifyDataSetChanged()

            var scrollTo = adapter.shownItems.firstOrNull {
                it.getTitle().toLowerCase()
                    .startsWith(search_et.text.toString().toLowerCase())
            }

            if (scrollTo != null){
                var index = adapter.shownItems.indexOf(scrollTo)
                if (index > 0) index--
                recyclerView.scrollToPosition(index)
            }else{
                recyclerView.scrollToPosition(0)
            }

        }

        recyclerView.adapter = adapter

        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager

        recyclerView.addItemDecoration(RecyclerMarginDecorator(mTopFirst = 200, mBottomLast = 64))

    }

    protected open fun setLayout(){
        setContentView(R.layout.basic_catalog_layout)
    }


}

