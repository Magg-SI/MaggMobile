package pl.tysia.maggstone.data.api.model

data class GetOrderRequest(val token : String, val dokID : Int) {
    val func = "readOneZamow"
}