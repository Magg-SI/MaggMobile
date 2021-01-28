package pl.tysia.maggstone.data.api.model

import com.google.gson.annotations.SerializedName

sealed class UpdatePictureRequest(val token: String ){
    class Start(val towID: Int, @SerializedName("foto") val photo : String, token: String) :
        UpdatePictureRequest(token) {
        val func: String = "addFotoStart"
    }

    class Next(val fotoID: Int, @SerializedName("foto") val photo : String, val pageNo : Int, token: String) :
        UpdatePictureRequest(token) {
        val func: String = "addFotoNext"
    }

    class Finalize(val fotoID: Int, token: String) : UpdatePictureRequest(token) {
        val func: String = "addFotoFinalize"
    }
}