package pl.tysia.maggstone.ui.presentation_logic.filterer

abstract class Filter<in T : IFilterable> {

    abstract fun fitsFilter(item: T): Boolean
}