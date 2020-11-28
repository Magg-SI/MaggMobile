package pl.tysia.maggstone.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import pl.tysia.maggstone.ui.presentation_logic.adapter.ICatalogable
import java.io.Serializable

@Entity
data class Contractor(@PrimaryKey var id : Int,
                      @ColumnInfo(name = "index") var index : String,
                      @ColumnInfo(name = "name") var name : String) : ICatalogable, Serializable {

    @SerializedName("licznikVer")
    var counter : Int? = null

    companion object{
        const val CONTRACTOR_EXTRA = "pl.tysia.maggstone.contractor_extra"
    }


    override fun getTitle(): String {
        return index
    }

    override fun getShortDescription(): String {
       return name
    }


}

