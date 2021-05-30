package pl.tysia.maggstone.data.model

import com.google.gson.annotations.SerializedName
import pl.tysia.maggstone.data.api.model.APIResponse
import pl.tysia.maggstone.ui.presentation_logic.adapter.ICatalogable
import java.io.Serializable

class Hose : ICatalogable, Serializable, APIResponse() {
    var cord : Ware? = null
    var tip1 : Ware? = null
    var tip2 : Ware? = null
    var sleeve : Ware? = null

    @SerializedName("ktrID")
    var ktrID : Int = 0
    @SerializedName("ilosc")
    var length : Int? = null
    var code : String? = null
    @SerializedName("katSkrecenia")
    var angle : String? = null
    @SerializedName("ktoWykonal")
    var creator : String? = null
    @SerializedName("kontrahent")
    var contractor : String? = null
    @SerializedName("nazwa")
    var name : String? = null
    var id: Int? = null
    @SerializedName("cena")
    var priceN: Double? = null
    var priceB: Double? = null
    @SerializedName("magazyn")
    var warehouse : String? = null
    @SerializedName("dataDok")
    var documentDate : String? = null
    @SerializedName("nrDok")
    var documentNumber : String? = null
    companion object{
        const val HOSE_EXTRA = "pl.tysia.maggstone.hose_extra"
    }

    override fun getTitle(): String = name!!

    override fun getSubtitle(): String {
        return  "Końcówki: ${tip1!!.index} / ${tip2!!.index}\n" +
                "Tulejki: ${sleeve!!.index}\n" +
                "Długość: ${length}";
    }

    override fun getDescription(): String{
        return "Wąż: ${cord!!.index}\n" +
                "Końcówki: ${tip1!!.index} / ${tip2!!.index}\n" +
                "Tulejki: ${sleeve!!.index}\n" +
                "Długość: ${length}\n" +
                "Kod węża: ${code}"
    }

    override fun getAdditionalInfo() = null

    override fun getFilteredValue(): String = name!!
}