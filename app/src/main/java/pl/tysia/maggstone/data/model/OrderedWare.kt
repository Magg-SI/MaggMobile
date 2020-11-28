package pl.tysia.maggstone.data.model

import com.google.gson.annotations.SerializedName

class OrderedWare(name: String) : Ware(name) {
    @SerializedName("ilZamow")
    var orderedNumber : Double? = null
    @SerializedName("ilZapak")
    var packedNumber : Double? = null
    @SerializedName("ilNext")
    var postponedNumber : Double? = null
    @SerializedName("ilAnul")
    var cancelledNumber : Double? = null
    @SerializedName("stanMag")
    var availability : Double? = null

}