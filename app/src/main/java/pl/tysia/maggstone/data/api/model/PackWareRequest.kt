package pl.tysia.maggstone.data.api.model

import com.google.gson.annotations.SerializedName

class PackWareRequest(
    @SerializedName("func")
    val func : String = "zapakOnePoz",
    @SerializedName("token")
    var token : String? = null,
    @SerializedName("pozyID")
    var id : Int? = null,
    @SerializedName("ilZapak")
    var packedNumber : Double? = null,
    @SerializedName("ilNext")
    var postponedNumber : Double? = null,
    @SerializedName("ilAnul")
    var cancelledNumber : Double? = null
    )