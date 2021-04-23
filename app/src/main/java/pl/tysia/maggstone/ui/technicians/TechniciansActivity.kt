package pl.tysia.maggstone.ui.technicians

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_technicians.*
import kotlinx.android.synthetic.main.activity_technicians.headerLayout
import kotlinx.android.synthetic.main.activity_technicians.recyclerView
import kotlinx.android.synthetic.main.activity_technicians.search_et
import pl.tysia.maggstone.R
import pl.tysia.maggstone.constants.Extras.TECHNICIANS
import pl.tysia.maggstone.data.NetAddressManager
import pl.tysia.maggstone.data.model.Technician
import pl.tysia.maggstone.data.model.Ware
import pl.tysia.maggstone.data.source.LoginDataSource
import pl.tysia.maggstone.data.source.LoginRepository
import pl.tysia.maggstone.okDialog
import pl.tysia.maggstone.ui.BaseActivity
import pl.tysia.maggstone.ui.RecyclerMarginDecorator
import pl.tysia.maggstone.ui.ViewModelFactory
import pl.tysia.maggstone.ui.login.afterTextChanged
import pl.tysia.maggstone.ui.presentation_logic.adapter.CatalogAdapter
import pl.tysia.maggstone.ui.presentation_logic.adapter.ICatalogable
import pl.tysia.maggstone.ui.presentation_logic.filterer.StringFilter
import pl.tysia.maggstone.ui.warehouses.WarehousesViewModel
import java.util.ArrayList

class TechniciansActivity : BaseActivity(), CatalogAdapter.ItemSelectedListener<ICatalogable>, SelectedTechnicianButton.OnButtonRemoveListener  {
    private lateinit var adapter : TechniciansAdapter
    private var filter: StringFilter<ICatalogable> = StringFilter<ICatalogable>(null){ filteredStrings, item ->
        filteredStrings.count { item.getFilteredValue().toLowerCase().contains(it.toLowerCase()) }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_technicians)

        adapter = TechniciansAdapter(ArrayList())

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

        recyclerView.addItemDecoration(RecyclerMarginDecorator(mTopFirst = 0, mBottomLast = 64))

        val techniciansViewModel = ViewModelProvider(this,
            ViewModelFactory(this)
        ).get(TechniciansViewModel::class.java)


        techniciansViewModel.orders.observe(this@TechniciansActivity, Observer {
            adapter.allItems.addAll(it)
            adapter.filter()
            adapter.notifyDataSetChanged()
            showBlockingProgress(false)
        })

        techniciansViewModel.techniciansResult.observe(this@TechniciansActivity, Observer {
            okDialog("Błąd", getString(it), this)
            showBlockingProgress(false)
        })

        techniciansViewModel.getTechnicians( LoginRepository(
            LoginDataSource(NetAddressManager(this)),
            this
        ).user!!.token)

        showBlockingProgress(true)
    }

    private fun refreshSelectedTechnicians(){
        flow.referencedIds.forEach {
                id -> headerLayout.removeView(findViewById(id))
        }

        adapter.selectedItems.forEach {
            val technician = it as Technician
            val technicianButton = SelectedTechnicianButton(technician, this)

            technicianButton.id = View.generateViewId()

            headerLayout.addView(technicianButton)
            flow.addView(technicianButton)
        }
    }

    fun onOk(view : View){
        val returnIntent = Intent()
        returnIntent.putExtra(TECHNICIANS, adapter.selectedItems)
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

    override fun onItemSelected(item: ICatalogable) {
        refreshSelectedTechnicians()
    }

    override fun onRemove(button: SelectedTechnicianButton) {
        adapter.selectedItems.remove(button.technician)
        refreshSelectedTechnicians()
        adapter.notifyDataSetChanged()
    }

}