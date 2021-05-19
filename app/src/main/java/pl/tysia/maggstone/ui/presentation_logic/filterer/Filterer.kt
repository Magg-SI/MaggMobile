package pl.tysia.maggstone.ui.presentation_logic.filterer

import java.util.*

abstract class Filterer<T : IFilterable>(var basicList: MutableList<T>, var filteredList: MutableList<T>) {

    var bestFit : Pair<T, Int>? = null
        private set

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
        } else {
            for (filterable in basicList) {
                var fits = true
                var fitness = 0

                for (filter in filters) {
                    if (!filter.fitsFilter(filterable)) {
                        fits = false
                        break
                    }

                   // fitness += filter.fitness(filterable)

                }

                if (fits) {
                    filteredList.add(filterable)
                    /*if (bestFit == null || fitness > bestFit!!.second){
                        bestFit = Pair(filterable, fitness)
                    }*/
                }
            }
        }
    }
}
