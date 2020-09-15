package pl.tysia.maggstone.ui.contractors

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pl.tysia.maggstone.R
import pl.tysia.maggstone.data.model.Contractor
import pl.tysia.maggstone.okDialog
import pl.tysia.maggstone.ui.login.afterTextChanged
import pl.tysia.maggstone.ui.presentation_logic.adapter.BasicCatalogAdapter
import pl.tysia.maggstone.ui.presentation_logic.adapter.CatalogAdapter
import pl.tysia.maggstone.ui.presentation_logic.adapter.ICatalogable
import pl.tysia.maggstone.ui.presentation_logic.filterer.StringFilter
import java.util.ArrayList


class ContractorsDialogFragment : DialogFragment(), CatalogAdapter.ItemSelectedListener<Contractor> {
    private lateinit var adapter : BasicCatalogAdapter
    private lateinit var recycler : RecyclerView
    private lateinit var okButton: Button
    private lateinit var filter: StringFilter

    private lateinit var contractorsViewModel: ContractorsViewModel

    private var onContractorSelectedListener: OnContractorSelectedListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (activity is OnContractorSelectedListener)
            onContractorSelectedListener = activity as OnContractorSelectedListener
    }

    override fun onStart() {
        super.onStart()

        val dialog = dialog
        if (dialog != null) {
            dialog!!.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = activity!!.layoutInflater

        val dialog = Dialog(activity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val view = inflater.inflate(R.layout.basic_catalog_layout, null)
        dialog.setContentView(view)

        recycler = view.findViewById(R.id.recyclerView)

        okButton = view.findViewById<Button>(R.id.ok_button)
        okButton.setOnClickListener {
            onContractorSelectedListener?.contractorSelected(adapter.selectedItem as Contractor)
            dismiss()
        }

        contractorsViewModel = ViewModelProvider(this, ContractorsViewModelFactory())
            .get(ContractorsViewModel::class.java)

        adapter = BasicCatalogAdapter(ArrayList())
        adapter.addItemSelectedListener(this)

        filter = StringFilter("")
        adapter.filterer.addFilter(filter)

        val searchET = view.findViewById<EditText>(R.id.search_et)
        searchET.afterTextChanged {
            filter.filteredString = searchET.text.toString()
            adapter.filter()
            adapter.notifyDataSetChanged()
        }

        recycler.adapter = adapter

        val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recycler.layoutManager = linearLayoutManager

        contractorsViewModel.contractors.observe(this, Observer {
            adapter.allItems.clear()
            adapter.addAll(contractorsViewModel.contractors.value as Collection<ICatalogable>?)
            adapter.filter()
            adapter.notifyDataSetChanged()
        })

        contractorsViewModel.contractorsResult.observe(this, Observer {
            val message = getString(contractorsViewModel.contractorsResult.value!!)
            activity?.let { it1 -> okDialog("Błąd", message, it1) }
        })

        contractorsViewModel.getContractors()

        return dialog
    }

    interface OnContractorSelectedListener{
        fun contractorSelected(contractor: Contractor)
    }


    companion object {

        @JvmStatic
        fun newInstance() =
            ContractorsDialogFragment()

    }

    override fun onItemSelected(item: Contractor?) {
        okButton.isEnabled = true
    }
}

