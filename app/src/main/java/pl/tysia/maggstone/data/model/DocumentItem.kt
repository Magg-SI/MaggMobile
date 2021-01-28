package pl.tysia.maggstone.data.model

import pl.tysia.maggstone.ui.presentation_logic.adapter.ICatalogable
import java.io.Serializable

data class DocumentItem(@Transient val item : ICatalogable) : ICatalogable, Serializable {
    var ilosc : Double = if (item is Hose) item.number!!.toDouble() else 1.0
    val towID : Int = if (item is Hose) item.id!! else if (item is Ware) item.id!! else -1

    fun getShortDescription() : String {
        return when (item) {
            is Hose -> item.name!!
            is Ware -> item.name
            else -> item.description
        }
    }

    override fun getTitle(): String {
        return when (item) {
            is Hose -> "Wąż ${item.id}"
            is Ware -> item.index!!
            else -> item.title
        }
    }

    override fun getDescription(): String {
        return when (item) {
            is Hose -> "Wąż: ${item.cord!!.index}\n" +
                    "Końcówki: ${item.tip1!!.index} / ${item.tip2!!.index}\n" +
                    "Tulejki: ${item.sleeve!!.index}\n" +
                    "Długość: ${item.length}\n" +
                    "Kod węża: ${item.code}"
            is Ware -> item.description
            else -> item.description
        }
    }

 /*   class WareItem(item: Ware) : DocumentItem<Ware>(1.0, item.id!!, item){
        override fun getShortDescription() : String = item.name
        override fun getTitle(): String = item.index!!
        override fun getDescription(): String = item.description
    }


    class HoseItem(item: Hose) : DocumentItem<Hose>(item.number!!.toDouble(), item.id!!, item){
        override fun getTitle(): String = "Wąż ${item.id}"
        override fun getShortDescription() : String = item.name!!
        override fun getDescription(): String {
            return "Wąż: ${item.cord!!.index}\n" +
                    "Końcówki: ${item.tip1!!.index} / ${item.tip2!!.index}\n" +
                    "Tulejki: ${item.sleeve!!.index}\n" +
                    "Długość: ${item.length}\n" +
                    "Kod węża: ${item.code}"
        }
    }*/


}