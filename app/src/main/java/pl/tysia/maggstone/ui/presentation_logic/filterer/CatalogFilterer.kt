package pl.tysia.maggstone.ui.presentation_logic.filterer

import pl.tysia.maggstone.ui.presentation_logic.adapter.ICatalogable
import java.util.ArrayList

class CatalogFilterer<T : ICatalogable>(basicList: ArrayList<T>, filteredList: ArrayList<T>) :
    Filterer<T>(basicList, filteredList)