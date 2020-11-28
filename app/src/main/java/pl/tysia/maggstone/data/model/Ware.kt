package pl.tysia.maggstone.data.model

import android.graphics.Bitmap
import androidx.room.*
import com.google.gson.annotations.SerializedName
import pl.tysia.maggstone.data.api.model.APIResponse
import pl.tysia.maggstone.ui.presentation_logic.adapter.ICatalogable
import java.io.Serializable

@Entity
open class Ware(@SerializedName("nazwa") var name: String) : Serializable, ICatalogable,
    APIResponse() {
    companion object{
        const val WARE_EXTRA = "pl.tysia.maggstone.ware_extra"
    }

    @SerializedName("towID", alternate = ["pozyID"])
    @PrimaryKey
    var id : Int? = null

    @SerializedName("licznikVer")
    var counter : Int? = null

    @SerializedName("kodQR ")
    var qrCode : String? = null

    @SerializedName("indeks")
    var index : String? = "index"

    @SerializedName("lokalizacja")
    var location : String? = null

    @SerializedName("cena")
    var price : Double? = null

    @SerializedName("stanyMagaz")
    @Ignore
    var availabilities : List<Availability>? = null

    @SerializedName("isFoto")
    var hasPhoto = false

    @Ignore
    @Transient
    var image : Bitmap? = null

    var photoString : String? = null

    @Ignore
    var photoPath : String? = null


    constructor(qrCode : String, id : Int, index : String, name: String, hasPhoto : Boolean,  location : String, price : Double) : this(name){
        this.qrCode = qrCode
        this.id = id
        this.index = index
        this.location = location
        this.price = price

        this.hasPhoto = hasPhoto
    }

    override fun getTitle(): String {
       return name
    }

    override fun getShortDescription(): String {
        return index!!
    }



}

