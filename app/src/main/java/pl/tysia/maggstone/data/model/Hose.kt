package pl.tysia.maggstone.data.model

import pl.tysia.maggstone.ui.presentation_logic.adapter.ICatalogable
import java.io.Serializable

class Hose : ICatalogable, Serializable {
    var cord : Ware? = null
    var tip1 : Ware? = null
    var tip2 : Ware? = null
    var sleeve : Ware? = null
    var length : Int? = null
    var code : String? = null
    var angle : String? = null
    var creator : String? = null

    var name : String? = null
    var id: Int? = null
    var priceN: Double? = null
    var priceB: Double? = null

    companion object{
        const val HOSE_EXTRA = "pl.tysia.maggstone.hose_extra"
    }

    override fun getTitle(): String = name!!

    override fun getDescription(): String{
        return "Wąż: ${cord!!.index}\n" +
                "Końcówki: ${tip1!!.index} / ${tip2!!.index}\n" +
                "Tulejki: ${sleeve!!.index}\n" +
                "Długość: ${length}\n" +
                "Kod węża: ${code}"
    }
}