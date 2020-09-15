package pl.tysia.maggstone.ui.presentation_logic.filterer

import pl.tysia.maggstone.ui.presentation_logic.adapter.ICatalogable


class StringFilter(var filteredString : String?) : Filter<ICatalogable>(){


    override fun fitsFilter(item: ICatalogable): Boolean {
        return if (filteredString == null)
            true
        else
            item.title.toLowerCase().contains(filteredString!!.toLowerCase())
    }

}