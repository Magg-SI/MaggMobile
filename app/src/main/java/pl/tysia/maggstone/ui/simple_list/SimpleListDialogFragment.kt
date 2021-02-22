package pl.tysia.maggstone.ui.simple_list

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pl.tysia.maggstone.R
import pl.tysia.maggstone.ui.RecyclerMarginDecorator
import pl.tysia.maggstone.ui.login.afterTextChanged
import pl.tysia.maggstone.ui.presentation_logic.adapter.BasicCatalogAdapter
import pl.tysia.maggstone.ui.presentation_logic.adapter.CatalogAdapter
import pl.tysia.maggstone.ui.presentation_logic.adapter.ICatalogable
import pl.tysia.maggstone.ui.presentation_logic.filterer.StringFilter
import java.util.ArrayList

private const val TITLE = "title"

class SimpleListDialogFragment : DialogFragment() , CatalogAdapter.ItemSelectedListener<ICatalogable> {
    private lateinit var searchET : EditText
    private lateinit var recycler : RecyclerView
    private lateinit var okButton : Button
    private lateinit var progressBar : ProgressBar

    private lateinit var adapter : BasicCatalogAdapter
    private lateinit var filter: StringFilter<ICatalogable>

    private var title: String? = null

    private lateinit var owner : SimpleListOwner<*>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString(TITLE)
        }

        if (activity is SimpleListOwner<*>)
            owner = activity as SimpleListOwner<*>
        else throw Exception("Owner Activity must implement SimpleListOwner interface")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        adapter = BasicCatalogAdapter(ArrayList())
        adapter.addItemSelectedListener(this)

        filter = StringFilter(""){ filteredString, item ->
            item.title.toLowerCase().contains(filteredString.toLowerCase())
        }

        adapter.filterer.addFilter(filter)

        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_simple_list_dialog, container, false).also{
            searchET = it.findViewById(R.id.search_et2)
            recycler = it.findViewById(R.id.recycler)
            okButton = it.findViewById(R.id.ok_button)
            progressBar = it.findViewById(R.id.progressBar2)

            searchET.afterTextChanged {
                filter.filteredString = searchET.text.toString()
                adapter.filter()
                adapter.notifyDataSetChanged()
            }

            recycler.adapter = adapter

            val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            recycler.layoutManager = linearLayoutManager

            recycler.addItemDecoration(RecyclerMarginDecorator())

       /*     okButton.setOnClickListener {
                owner.onItemSelected(adapter.selectedItem, tag!!)
                dismiss()
            }*/
        }
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            dialog.window
                ?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        owner.getLiveData(tag!!)!!.observe(this, Observer {
            adapter.allItems.clear()
            adapter.addAll(it as Collection<ICatalogable>?)
            adapter.filter()
            adapter.notifyDataSetChanged()
            progressBar.visibility = View.GONE
        })
    }

    override fun onItemSelected(item: ICatalogable?) {
        owner.onItemSelected(adapter.selectedItem, tag!!)
        dismiss()
    }

    companion object {
        @JvmStatic
        fun newInstance(title: String) =
            SimpleListDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(TITLE, title)
                }
            }
    }

    interface SimpleListOwner<T : ICatalogable>{
        fun onItemSelected(item: ICatalogable, tag : String)

        fun getLiveData(tag : String) : LiveData<List<T>>?
    }


}