package pl.tysia.maggstone.data.api.model

import com.google.gson.annotations.SerializedName

data class UpdatePictureRequest(val func: String = "addFoto",
                                val token: String ,
                                val towID: Int,
                                @SerializedName("foto")
                                val photo : String)