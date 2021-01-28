package pl.tysia.maggstone.data.api.model

import com.google.gson.annotations.SerializedName
import pl.tysia.maggstone.data.model.*
import retrofit2.Retrofit


open class APIResponse {
    companion object{
        const val RESPONSE_OK = 0
        const val ERR_USERNAME = 1
        const val ERR_PASSWORD = 2
    }

    @SerializedName("retCode")
    var retCode : Int? = null
    @SerializedName("retOpis")
    var retMessage : String? = null

    class GetOrders : APIResponse() {
        @SerializedName("listaZamow")
        var orders : ArrayList<Order>? = null
    }

    class GetOrderedWares : APIResponse() {
        @SerializedName("readOneZamow")
        var wares : ArrayList<OrderedWare>? = null
    }

    class WaresPage : APIResponse() {
        @SerializedName("lista")
        var list : ArrayList<Ware>? = null
    }

    class ContractorsPage : APIResponse() {
        @SerializedName("lista")
        var list : ArrayList<Contractor>? = null
    }

    class Pages : APIResponse() {
        @SerializedName("pageSize")
        var pageSize : Int? = null
        @SerializedName("pageCount")
        var pageCount : Int? = null
        @SerializedName("listaID")
        var listID : Int? = null
    }

    class Picture : APIResponse(){
        @SerializedName("foto")
        var picture: String? = null
    }

    class Availabilities : APIResponse(){
        @SerializedName("stanyMagaz")
        var availabilities : ArrayList<Availability>? = null
    }

    class Hose : APIResponse(){
        var nazwa: String? = null
        var wazID: Int? = null
        var cenaN: Double? = null
        var cenaB: Double? = null
    }

    class Photo : APIResponse(){
        var fotoID: Int? = null
    }

    class Login : APIResponse() {
        @SerializedName("userID")
        var userID: Int? = null

        @SerializedName("token")
        var token: String? = null
    }

}