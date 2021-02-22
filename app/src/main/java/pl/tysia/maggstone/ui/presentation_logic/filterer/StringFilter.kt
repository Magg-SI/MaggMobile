package pl.tysia.maggstone.ui.presentation_logic.filterer

import pl.tysia.maggstone.ui.presentation_logic.adapter.ICatalogable


class StringFilter<T : ICatalogable>(var filteredString : String?,
                                     private var predicate : (String, T) -> Boolean) : Filter<T>(){


    override fun fitsFilter(item: T): Boolean {
        return if (filteredString == null)
            true
        else
            predicate(filteredString!!, item)
    }

}