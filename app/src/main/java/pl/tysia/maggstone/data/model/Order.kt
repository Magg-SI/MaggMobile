package pl.tysia.maggstone.data.model

import android.graphics.Bitmap
import pl.tysia.maggstone.ui.presentation_logic.adapter.ICatalogable
import java.io.Serializable

data class Order(var id: Int, var warehouse: String, var documentDate: String, var documentNr: String, var comments: String)
    : ICatalogable, Serializable{

    var packed = false

    override fun getTitle() = "Zamówienie nr $documentNr \nz dnia $documentDate"

    override fun getShortDescription(): String {
        var res =  "Magazyn: $warehouse\n"
        if (comments != "null") res += "\nUwagi: "+ comments

        return res
    }


}