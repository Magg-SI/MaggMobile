package pl.tysia.maggstone.data.api.model

import com.google.gson.annotations.SerializedName

class GetAvailabilitiesRequest(val token : String,
                               @SerializedName("indeks")
                               val index : String) {
    val func = "getStanyMagaz"
}