package pl.tysia.maggstone.data.api.model

import com.google.gson.annotations.SerializedName
import pl.tysia.maggstone.data.model.*


open class APIResponse {
    companion object{
        const val RESPONSE_OK = 0
        const val ERR_USERNAME = 1
        const val ERR_PASSWORD = 2

        const val WORKER_IN_PROGRESS = 1
    }

    @SerializedName("retCode")
    var retCode : Int? = null
    @SerializedName("retOpis")
    var retMessage : String? = null

    class GetOrders : APIResponse() {
        @SerializedName("listaZamow")
        var orders : ArrayList<Order>? = null
    }

    class GetPriceList : APIResponse() {
        @SerializedName("cennik")
        var priceList : ArrayList<ServicePrice>? = null
    }

    class Stocktaking : APIResponse() {
        @SerializedName("dokID")
        var documentID : Int? = null

        @SerializedName("pozycje")
        var wares : ArrayList<SimpleWare>? = null

        class SimpleWare{
            var towID : Int? = null
            var ilosc : Double? = null
        }
    }

    class TestStocktakingPosition : APIResponse() {
        @SerializedName("ilosc")
        var count : Double? = null
    }

    class Document : APIResponse(){
        var workerID : Int? = null
    }

    class Worker : APIResponse(){
        var procent : Int? = null
    }

    class WarePrice : APIResponse(){
        var cena : Double? = null
        var rabat : Double? = null
    }

    class GetOrderedWares : APIResponse() {
        @SerializedName("readOneZamow")
        var wares : ArrayList<OrderedWare>? = null
    }

    class Warehouses : APIResponse() {
        @SerializedName("lista")
        var warehouses  : ArrayList<Warehouse>? = null
    }

    class Technicians : APIResponse() {
        @SerializedName("lista")
        var technicians  : ArrayList<Technician>? = null
    }

    class Service : APIResponse() {
        @SerializedName("nazwa")
        var name : String? = null

        @SerializedName("cenaN")
        var priceN : Double? = null

        @SerializedName("cenaB")
        var priceB : Double? = null

        @SerializedName("serwisID")
        var id : Int? = null
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


    class LocationChange : APIResponse(){
        @SerializedName("indeks")
        var index: String? = null
    }

    class Availabilities : APIResponse(){
        @SerializedName("stanyMagaz")
        var availabilities : ArrayList<Availability>? = null

        @SerializedName("ilMin")
        var ilMin: Double? = null

        @SerializedName("ilMax")
        var ilMax: Double? = null
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

    class CooperationHistory : APIResponse() {
        @SerializedName("lista")
        var cooperationHistory: ArrayList<pl.tysia.maggstone.data.model.Document>? = null
    }

}