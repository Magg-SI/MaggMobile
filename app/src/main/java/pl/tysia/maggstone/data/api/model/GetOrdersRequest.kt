package pl.tysia.maggstone.data.api.model

data class GetOrdersRequest(val token : String) {
    val func = "listaZamow"
}