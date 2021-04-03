package pl.tysia.maggstone.ui.presentation_logic.filterer

import android.widget.Filterable
import pl.tysia.maggstone.ui.presentation_logic.adapter.ICatalogable


class StringFilter<in T : ICatalogable>(var filteredStrings : List<String>?,
                                     private var predicate : ( List<String>, T) -> Int) : Filter<T>(){


    override fun fitsFilter(item: T): Boolean {
        return if (filteredStrings == null)
            true
        else
            predicate(filteredStrings!!, item) == filteredStrings!!.size
    }

    override fun fitness(item: T): Int {
        return if (filteredStrings == null)
            1
        else
            predicate(filteredStrings!!, item)
    }

}