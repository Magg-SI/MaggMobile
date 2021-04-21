package pl.tysia.maggstone.data.model

import pl.tysia.maggstone.constants.Warehouses.WAREHOUSE_MAIN
import pl.tysia.maggstone.ui.presentation_logic.adapter.ICatalogable
import java.io.Serializable

data class DocumentItem(@Transient val item : ICatalogable) : ICatalogable, Serializable {
    var ilosc : Double = 1.0
    var iloscOk : Int  = 0
    val towID : Int = if (item is Hose) item.id!! else if (item is Ware) item.id!! else -1

    fun getShortDescription() : String {
        return when (item) {
            is Hose -> item.cord!!.name
            is Ware -> item.name
            else -> item.getDescription()
        }
    }

    fun getAvailabilities() : List<Availability>?{
        return when (item) {
            is Hose -> null
            is Ware -> item.availabilities
            else -> null
        }
    }

    fun getMainAvailability() : Availability?{
        return getAvailabilities()?.get(0)
    }

    override fun getTitle(): String {
        return when (item) {
            is Hose -> item.cord!!.index!!
            is Ware -> item.index!!
            else -> item.getTitle()
        }
    }

    override fun getSubtitle(): String {
        return ""
    }

    override fun getDescription(): String {
        return when (item) {
            is Hose ->
                "Końcówki: ${item.tip1!!.index} / ${item.tip2!!.index}\n" +
                        "Tulejki: ${item.sleeve!!.index}\n" +
                        "Długość: ${item.length}\n" +
                        "Cena brutto: ${item.priceB}\n" +
                        "Cena netto: ${item.priceN}"

            is Ware -> item.getDescription()
            else -> item.getDescription()
        }
    }

    override fun getAdditionalInfo() = null

    override fun getFilteredValue(): String {
        return item.getFilteredValue()
    }


}