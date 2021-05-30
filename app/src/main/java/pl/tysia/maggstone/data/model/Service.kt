package pl.tysia.maggstone.data.model

import com.google.gson.annotations.SerializedName
import pl.tysia.maggstone.data.api.model.APIRequest
import pl.tysia.maggstone.ui.presentation_logic.adapter.ICatalogable
import java.io.Serializable
import java.lang.StringBuilder

class Service() : APIRequest("addSerwis"), Serializable, ICatalogable {
    companion object{
        const val SERVICE_EXTRA ="pl.tysia.maggstone.service"
    }

    @SerializedName("ktrID")
    var ktrID : Int = 0

    @SerializedName("ileOsob")
    var peopleCount : Int = 0

    @SerializedName("ileKm")
    var kilometers : Int = 0

    @SerializedName("dataSerwisu")
    var date : String? = null

    @SerializedName("godzinaOd")
    var startTime : String? = null

    @SerializedName("godzinaDo")
    var endTime : String? = null

    @SerializedName("czyDojazd")
    var commuting : Int? = null

    @SerializedName("czySobota")
    var saturday : Int? = null

    @SerializedName("czySwieto")
    var holiday : Int? = null

    @SerializedName("listaPracow")
    var technicians : ArrayList<Technician>? = null

    var name : String? = null
    var priceN : Double? = null
    var priceB : Double? = null
    var id : Int? = null

    override fun getTitle(): String {
        return "(${peopleCount} os.), $startTime - $endTime"
    }

    override fun getSubtitle(): String {
        return name!!
    }

    override fun getDescription(): String {
        val sb = StringBuilder()

        if (priceN != null){
            sb.append("Cena netto: $priceN\n")
        }

        if (priceB != null){
            sb.append("Cena brutto: $priceB")
        }

        return sb.toString()
    }

    override fun getAdditionalInfo(): String? {
        return ""
    }

    override fun getFilteredValue(): String {
        return name!!
    }
}