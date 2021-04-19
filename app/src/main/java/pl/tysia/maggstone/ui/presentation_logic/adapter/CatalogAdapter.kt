package pl.tysia.maggstone.ui.presentation_logic.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import pl.tysia.maggstone.ui.presentation_logic.filterer.CatalogFilterer
import pl.tysia.maggstone.ui.presentation_logic.filterer.Filterer
import java.util.*

abstract class CatalogAdapter<T : ICatalogable, H : RecyclerView.ViewHolder>(var allItems: ArrayList<T>) :
    RecyclerView.Adapter<H>() {

    protected var shownItems: ArrayList<T> = ArrayList()
    var filterer: Filterer<T>
    var selectedItem: T? = null

    val listeners: ArrayList<ItemSelectedListener<T>>
    protected var emptyListeners: ArrayList<EmptyListListener>

    protected open fun onItemClick(v: View?, adapterPosition: Int) {
        val item = shownItems[adapterPosition]
        selectedItem = item
        notifyDataSetChanged()
        for (listener in listeners) listener.onItemSelected(item)
    }

    fun addItemSelectedListener(listener: ItemSelectedListener<T>) {
        listeners.add(listener)
    }

    fun addEmptyListener(listener: EmptyListListener) {
        emptyListeners.add(listener)
    }

    fun addItem(item: T) {
        allItems.add(0, item)
    }

    fun addAll(items: Collection<T>?) {
        allItems.addAll(items!!)
    }

    fun filter() {
        filterer.filter()
    }

    override fun getItemCount(): Int {
        return shownItems.size
    }

    interface ItemSelectedListener<T : ICatalogable?> {
        fun onItemSelected(item: T)
    }

    interface EmptyListListener {
        fun onListEmptied()
    }

    init {
        filterer = CatalogFilterer<T>(allItems, shownItems)
        listeners = ArrayList()
        emptyListeners = ArrayList()
    }
}