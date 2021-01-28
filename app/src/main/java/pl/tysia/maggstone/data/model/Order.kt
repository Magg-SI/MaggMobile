package pl.tysia.maggstone.data.model

import android.graphics.Bitmap
import com.google.gson.annotations.SerializedName
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

    @Transient
    var packed = false

    companion object{
        const val ORDER_EXTRA = "pl.tysia.maggstone.order"
    }

    override fun getTitle() = "Magazyn: $warehouse"

    override fun getDescription(): String {
        var res =  "Numer: $documentNr\n" +
                "Data: $documentDate\n"
        if (comments != "null") res += "\nUwagi: "+ comments

        return res
    }


}