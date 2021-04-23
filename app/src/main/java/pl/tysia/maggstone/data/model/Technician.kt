package pl.tysia.maggstone.data.model

import com.google.gson.annotations.SerializedName
import pl.tysia.maggstone.ui.presentation_logic.adapter.ICatalogable
import java.io.Serializable

class Technician : ICatalogable, Serializable {
    @SerializedName("ID")
    var id : Int? = null

    @SerializedName("nazwa")
    var name : String? = null

    override fun getTitle(): String {
        return name!!
    }

    override fun getSubtitle(): String {
        return ""
    }

    override fun getDescription(): String {
        return ""
    }

    override fun getAdditionalInfo(): String? {
        return null
    }

    override fun getFilteredValue(): String {
        return name!!
    }
}