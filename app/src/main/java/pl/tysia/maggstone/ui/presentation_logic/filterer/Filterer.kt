package pl.tysia.maggstone.ui.presentation_logic.filterer

import java.util.*

abstract class Filterer<T : IFilterable>(var basicList: MutableList<T>, var filteredList: MutableList<T>) {

    private var filters: LinkedList<Filter<T>> = LinkedList()

    fun addFilter(filter: Filter<T>) {
        filters.add(filter)
        filter()
    }

    fun removeFilter(filter: Filter<T>) {
        filters.remove(filter)
        filter()
    }

    fun notifyFilterChange() {
        filter()
    }

    fun filter() {
        filteredList.clear()

        if (filters.isEmpty()) {
            filteredList.addAll(basicList)
        }
        else {
            for (filterable in basicList) {
                var fits = true

                for (filter in filters) {
                    if (!filter.fitsFilter(filterable))
                        fits = false
                    break
                }

                if (fits)
                    filteredList.add(filterable)

            }
        }
    }
}
