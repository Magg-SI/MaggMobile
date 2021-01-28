package pl.tysia.maggstone.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import pl.tysia.maggstone.ui.presentation_logic.adapter.ICatalogable
import java.io.Serializable

@Entity
data class Contractor(@SerializedName("ktrID") @PrimaryKey var id : Int,
                      @SerializedName("symbol") @ColumnInfo(name = "index") var index : String,
                      @SerializedName("nazwa") @ColumnInfo(name = "name") var name : String?,
                      @SerializedName("adres") @ColumnInfo(name = "address") var address : String?,
                      @SerializedName("nip") @ColumnInfo(name = "nip") var nip : String?) : ICatalogable, Serializable {

    @SerializedName("licznikVer")
    var counter : Int? = null

    companion object{
        const val CONTRACTOR_EXTRA = "pl.tysia.maggstone.contractor_extra"
    }


    override fun getTitle(): String {
        return index
    }

    override fun getDescription(): String {
       return if (name != null) name!! else ""
    }


}

