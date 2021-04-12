package pl.tysia.maggstone.data.model

import com.google.gson.annotations.SerializedName
import pl.tysia.maggstone.ui.presentation_logic.adapter.ICatalogable
import java.io.Serializable

class Warehouse(@SerializedName("ID") val id : Int, @SerializedName("nazwa") val name : String) : Serializable, ICatalogable {
    companion object {
        const val WAREHOUSE_EXTRA = "pl.tysia.maggstone.warehouse_extra"

    }

    override fun getTitle() = name

    override fun getSubtitle() = ""

    override fun getDescription() = ""

    override fun getAdditionalInfo() = ""

    override fun getFilteredValue() = name
}