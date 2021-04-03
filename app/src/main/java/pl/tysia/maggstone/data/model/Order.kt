package pl.tysia.maggstone.data.model

import android.graphics.Bitmap
import com.google.gson.annotations.SerializedName
import pl.tysia.maggstone.constants.PackingState
import pl.tysia.maggstone.ui.presentation_logic.adapter.ICatalogable
import java.io.Serializable

data class Order(
    @SerializedName("dokID")
    var id: Int,
    @SerializedName("magazyn")
    var warehouse: String,
    @SerializedName("dataDok")
    var documentDate: String,
    @SerializedName("nrDok")
    var documentNr: String,
    @SerializedName("uwagi")
    var comments: String)
    : ICatalogable, Serializable{

    @SerializedName("ilePozZ")
    var packedItems = 5;

    @SerializedName("ilePoz")
    var totalItems = 10;

    fun getPackingState() : PackingState{
        return when {
            packedItems >= totalItems -> PackingState.PACKED
            packedItems == 0 -> PackingState.UNTOUCHED
            else -> PackingState.STARTED
        }
    }

    companion object{
        const val ORDER_EXTRA = "pl.tysia.maggstone.order"
    }

    override fun getTitle() = "Magazyn: $warehouse"
    override fun getSubtitle(): String {
        return ""
    }

    override fun getDescription(): String {
        var res =  "Numer: $documentNr\n" +
                "Data: $documentDate\n"
        if (comments != null) res += "\nUwagi: $comments"

        return res
    }

    override fun getAdditionalInfo() = null

    override fun getFilteredValue(): String {
        return warehouse+documentDate+documentNr+comments
    }


}