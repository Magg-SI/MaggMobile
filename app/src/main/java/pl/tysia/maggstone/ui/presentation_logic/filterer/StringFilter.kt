package pl.tysia.maggstone.ui.presentation_logic.filterer

import android.widget.Filterable
import pl.tysia.maggstone.ui.presentation_logic.adapter.ICatalogable


class StringFilter<in T : ICatalogable> : Filter<T>(){

    var filteredString : String? = null
        set(value) {
            field = value

            filteredStrings = value?.split(" ")
        }

    private var filteredStrings : List<String>? = null

    override fun fitsFilter(item: T): Boolean {
        return if (filteredStrings == null) {
            true
        }else {
            filteredStrings!!.count { item.getFilteredValue().toLowerCase().contains(it.toLowerCase())} == filteredStrings!!.size
        }
    }

    override fun fitness(item: T): Int {
        return if (filteredStrings == null)
            1
        else
            filteredStrings!!.count { item.getFilteredValue().toLowerCase().contains(it.toLowerCase())}
    }

}