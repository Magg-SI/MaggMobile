package pl.tysia.maggstone.data.api.model

import com.google.gson.annotations.SerializedName

data class FindWareRequest(
    val func : String = "findTowarBy",
    val token : String,
    val keyValue : String,
    val keyCode : String){

    companion object{
        const val KEY_QR = "QR"
        const val KEY_ID = "ID"
        const val KEY_INDEX = "Indeks"
    }
}