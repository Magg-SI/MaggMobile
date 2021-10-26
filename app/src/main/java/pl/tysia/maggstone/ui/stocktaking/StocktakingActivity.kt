package pl.tysia.maggstone.ui.stocktaking

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_new_document.*
import pl.tysia.maggstone.R
import pl.tysia.maggstone.app.MaggApp
import pl.tysia.maggstone.constants.ListActivityMode
import pl.tysia.maggstone.data.model.DocumentItem
import pl.tysia.maggstone.data.model.Ware
import pl.tysia.maggstone.okDialog
import pl.tysia.maggstone.ui.document.NewDocumentActivity
import pl.tysia.maggstone.ui.presentation_logic.adapter.DocumentAdapter
import pl.tysia.maggstone.ui.presentation_logic.adapter.StocktakingDocumentAdapter
import pl.tysia.maggstone.ui.showYesNoDialog
import pl.tysia.maggstone.ui.warehouses.WarehousesListActivity
import java.lang.Exception
import javax.inject.Inject

class StocktakingActivity : NewDocumentActivity(), StocktakingDocumentAdapter.OnEditConfirmedListener {
    @Inject lateinit var stocktakingViewModel : StocktakingViewModel

    private var documentID = -1

    override fun save() {
        throw Exception("Save method should not be used in StocktakingActivity. Items are saved before being added to list.")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        //setContentView(R.layout.activity_new_shift_document)
        setContentView(R.layout.activity_stocktaking)

        super.onCreate(savedInstanceState)

        (application as MaggApp).appComponent.inject(this)

        linearLayoutManager.stackFromEnd = true

        stocktakingViewModel.stocktakingDocument.observe(this@StocktakingActivity, Observer {
            documentID = it.documentID

            adapter.addAll(it.wares)
            adapter.filter()
            adapter.notifyDataSetChanged()

            if (it.notFoundItems > 0){
                okDialog(getString(R.string.couldnt_find_given_number_of_wares, it.notFoundItems),
                    getString(R.string.couldnt_find_wares_desc),
                this)
            }

            showBlockingLoading(false)
        })

        stocktakingViewModel.result.observe(this@StocktakingActivity, Observer {
            Toast.makeText(applicationContext, it, Toast.LENGTH_LONG).show()
            showBlockingLoading(false)
            if (documentID == -1) {
                finish()
            }
        })

        stocktakingViewModel.testResult.observe(this@StocktakingActivity, Observer { count ->
            adapter as StocktakingDocumentAdapter

            val editedItem = (adapter as StocktakingDocumentAdapter<DocumentItem>).editedItem!!

            editedItem.alreadyAdded = count

            if (count > 0){
                showYesNoDialog(this@StocktakingActivity,
                    "Do inwentaryzacji dodano już ${editedItem.item.item.getTitle()} w ilości $count",
                "Czy chcesz dodać kolejne ${editedItem.quantity}?",
                    { addWare(); it.dismiss() },
                    {it.dismiss()})
            }else{
                addWare()
            }

            showBlockingLoading(false)
        })

        stocktakingViewModel.wareResult.observe(this@StocktakingActivity, Observer {
            if (it == true){
                (adapter as StocktakingDocumentAdapter).acceptChanges()
            }
            showBlockingLoading(false)
        })

        showBlockingLoading(true)
        stocktakingViewModel.getStocktakingDocument()

        (adapter as StocktakingDocumentAdapter).listener = this
        adapter.addChangeListener(this)
    }

    override fun onListChanged() {

    }

    private fun addWare(){
        val editedItem = (adapter as StocktakingDocumentAdapter<DocumentItem>).editedItem!!

        stocktakingViewModel.addDocumentItem(documentID, editedItem.item.towID, editedItem.quantity)

        showBlockingLoading(true)
    }

    override fun onSearch() {
        val intent = Intent(this, WarehousesListActivity::class.java)
        intent.putExtra(ListActivityMode.LIST_ACTIVITY_MODE_EXTRA, ListActivityMode.SELECT)
        startActivityForResult(intent, WAREHOUSE_REQUEST_CODE)
    }

    override fun saveAllowed(): Boolean {
        return false
    }

    override fun getDocumentAdapter(): DocumentAdapter<DocumentItem> {
        return StocktakingDocumentAdapter(ArrayList())
    }

    override fun addWare(ware: Ware) {
        val item = DocumentItem(ware)

        adapter.addItem(item)

        wares_recycler.smoothScrollToPosition(adapter.itemCount)
    }

    override fun editConfirmed(item: StocktakingDocumentAdapter.EditedItem) {
        if (!item.new){
            stocktakingViewModel.updateDocumentItem(documentID, item.item.towID, item.quantity)
        }else{
            stocktakingViewModel.testStocktakingPosition(documentID, item.item.towID)
        }

        showBlockingLoading(true)
    }

}