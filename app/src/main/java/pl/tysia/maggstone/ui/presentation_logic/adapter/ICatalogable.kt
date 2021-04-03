package pl.tysia.maggstone.ui.presentation_logic.adapter

import pl.tysia.maggstone.ui.presentation_logic.filterer.IFilterable

interface ICatalogable : IFilterable {
    fun getTitle() : String
    fun getSubtitle() : String

    fun getDescription() : String
    fun getAdditionalInfo() : String?
}