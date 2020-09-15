package pl.tysia.maggstone.data.model

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import pl.tysia.maggstone.ui.presentation_logic.adapter.ICatalogable
import java.io.Serializable

@Entity
data class Ware(@ColumnInfo(name = "name") var name: String) : Serializable, ICatalogable {
    companion object{
        const val WARE_EXTRA = "pl.tysia.maggstone.ware_extra"
    }

    @PrimaryKey
    var id : Int? = null
    @ColumnInfo(name = "photoPath")
    var photoPath : String? = null
    @ColumnInfo(name = "qrCode")
    var qrCode : String? = null
    @ColumnInfo(name = "index")
    var index : String? = "index"
    @ColumnInfo(name = "location")
    var location : String? = null
    @ColumnInfo(name = "price")
    var price : Double? = null
    //@ColumnInfo(name = "availabilities")
    // var availabilities : List<Availability>? = null
    @ColumnInfo(name = "hasPhoto")
    var hasPhoto = false

    @Ignore
    var imageBitmap : Bitmap? = null;

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

