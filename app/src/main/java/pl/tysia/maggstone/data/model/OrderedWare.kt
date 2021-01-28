package pl.tysia.maggstone.data.model

import com.google.gson.annotations.SerializedName

class OrderedWare(name: String) : Ware(name) {
    @SerializedName("ilZamow")
    var orderedNumber : Double = 0.0
    @SerializedName("ilZapak")
    var packedNumber : Double = 0.0
    @SerializedName("ilNext")
    var postponedNumber : Double = 0.0
    @SerializedName("ilAnul")
    var cancelledNumber : Double = 0.0
    @SerializedName("stanMag")
    var availability : Double? = null

    var packed = orderedNumber <= ( packedNumber + postponedNumber + cancelledNumber)

}