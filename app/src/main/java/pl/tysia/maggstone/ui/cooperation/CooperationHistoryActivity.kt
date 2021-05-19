package pl.tysia.maggstone.ui.cooperation

import android.os.Bundle
import androidx.lifecycle.Observer
import pl.tysia.maggstone.app.MaggApp
import pl.tysia.maggstone.constants.Extras
import pl.tysia.maggstone.data.model.Contractor
import pl.tysia.maggstone.okDialog
import pl.tysia.maggstone.ui.presentation_logic.adapter.ICatalogable
import pl.tysia.maggstone.ui.simple_list.SimpleListActivity
import javax.inject.Inject

class CooperationHistoryActivity : SimpleListActivity() {
    private lateinit var contractor : Contractor

    @Inject
    lateinit var viewModel : ContractorsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showBlockingLoading(true)

        (application as MaggApp).appComponent.inject(this)

        contractor = intent.getSerializableExtra(Extras.CONTRACTOR_EXTRA) as Contractor

        viewModel.cooperationHistory.observe(this@CooperationHistoryActivity, Observer {
            adapter.allItems.addAll(it as Collection<ICatalogable>)
            adapter.filter()
            adapter.notifyDataSetChanged()
            showBlockingLoading(false)
        })

        viewModel.contractorsResult.observe(this@CooperationHistoryActivity, Observer {
            okDialog("Błąd", getString(it), this)
            showBlockingLoading(false)
        })

        viewModel.getCooperationHistory(contractor.id)
    }

    override fun onItemSelected(item: ICatalogable) {

    }
}