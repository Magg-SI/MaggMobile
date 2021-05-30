package pl.tysia.maggstone.data.model

import android.graphics.Bitmap
import androidx.room.*
import com.google.gson.annotations.SerializedName
import pl.tysia.maggstone.data.api.model.APIResponse
import pl.tysia.maggstone.ui.presentation_logic.adapter.ICatalogable
import java.io.Serializable
import java.lang.StringBuilder

@Entity
open class Ware(@SerializedName("nazwa") var name: String) : Serializable, ICatalogable, APIResponse() {
    companion object{
        const val WARE_EXTRA = "pl.tysia.maggstone.ware_extra"

        const val HOSE_TYPE_CORD = "P"
        const val HOSE_TYPE_SLEEVE = "T"
        const val HOSE_TYPE_TIP = "K"
    }

    @SerializedName("towID", alternate = ["pozyID"])
    @PrimaryKey
    var id : Int? = null

    @SerializedName("licznikVer")
    var counter : Int? = null

    @SerializedName("kodQR")
    var qrCode : String? = null

    @SerializedName("indeks")
    var index : String? = "index"

    @SerializedName("lokalizacja")
    var location : String? = null

    @SerializedName("cenaN")
    var priceN : Double? = null

    @SerializedName("cenaB")
    var priceB : Double? = null

    @Ignore
    var availabilities : List<Availability>? = null

    @SerializedName("fotoID")
    var photoID : Int? = null

    @Ignore
    var photoPath : String? = null

    @SerializedName("typ", alternate = ["wazTyp"])
    var hoseType : String? = null

    @SerializedName("wazFi")
    var hoseFi : String? = null

    @SerializedName("wazIdx")
    var hoseIdx : String? = null


    override fun getTitle(): String {
        val sb = StringBuilder()

        sb.append(index)

        /*if (location != null){
            sb.append(" - ")
            sb.append(location)
        }*/

       return sb.toString()
    }

    override fun getSubtitle(): String {
        //return if (!hoseType.isNullOrEmpty()) "${name}, Fi: $hoseFi"
        //else name
        return name
    }

    override fun getDescription(): String {
        val sb = StringBuilder()

        if (priceN != null){
            sb.append("Cena netto: $priceN\n")
        }

        if (priceB != null){
            sb.append("Cena brutto: $priceB")
        }

        return sb.toString()
    }

    override fun getAdditionalInfo() = location

    override fun getFilteredValue(): String {
        return name+location+index
    }


}

