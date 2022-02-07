package pl.tysia.maggstone.data.model

import android.graphics.Bitmap
import com.google.gson.annotations.SerializedName
import pl.tysia.maggstone.constants.PackingState
import pl.tysia.maggstone.ui.presentation_logic.adapter.ICatalogable
import java.io.Serializable

data class ServicePrice(
    @SerializedName("opis")
    var descrition: String,
    @SerializedName("cena")
    var price: Int,
    @SerializedName("jm")
    var jm: String )
    : ICatalogable, Serializable{

    companion object{
        const val SERVICE_PRICE_EXTRA = "pl.tysia.maggstone.serviceprice"
    }

    override fun getTitle() = "$price $jm"
    override fun getSubtitle(): String {
        return ""
    }

    override fun getDescription(): String {
        var res =  "$descrition"
        return res
    }

    override fun getAdditionalInfo() = null

    override fun getFilteredValue(): String {
        return descrition
    }
}