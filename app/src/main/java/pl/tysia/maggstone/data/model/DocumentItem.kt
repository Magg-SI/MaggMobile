package pl.tysia.maggstone.data.model

import pl.tysia.maggstone.ui.presentation_logic.adapter.ICatalogable
import java.io.Serializable

data class DocumentItem(@Transient val item : ICatalogable) : ICatalogable, Serializable {
    var ilosc : Double = 1.0
    val towID : Int = if (item is Hose) item.id!! else if (item is Ware) item.id!! else -1

    fun getShortDescription() : String {
        return when (item) {
            is Hose -> item.cord!!.name
            is Ware -> item.name
            else -> item.description
        }
    }

    override fun getTitle(): String {
        return when (item) {
            is Hose -> item.cord!!.index!!
            is Ware -> item.index!!
            else -> item.title
        }
    }

    override fun getDescription(): String {
        return when (item) {
            is Hose ->
                "Końcówki: ${item.tip1!!.index} / ${item.tip2!!.index}\n" +
                        "Tulejki: ${item.sleeve!!.index}\n" +
                        "Długość: ${item.length}\n" +
                        "Cena brutto: ${item.priceB}\n" +
                        "Cena netto: ${item.priceN}"

            is Ware -> item.description
            else -> item.description
        }
    }

}