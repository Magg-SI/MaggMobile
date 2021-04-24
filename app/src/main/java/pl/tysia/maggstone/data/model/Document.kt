package pl.tysia.maggstone.data.model

import pl.tysia.maggstone.ui.presentation_logic.adapter.ICatalogable
import java.io.Serializable

class Document(val dataDok: String,
                val nrDok: String,
                val netto: Double,
                val brutto: Double,
                val formaPl: String) : ICatalogable, Serializable{

    override fun getTitle(): String {
        return nrDok
    }

    override fun getSubtitle(): String {
        return dataDok
    }

    override fun getDescription(): String {
        return "Cena netto: $netto\nCena brutto: $brutto"
    }

    override fun getAdditionalInfo(): String {
        return formaPl
    }

    override fun getFilteredValue(): String {
        return  dataDok + nrDok
    }
}