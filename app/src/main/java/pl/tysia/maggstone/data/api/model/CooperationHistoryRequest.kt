package pl.tysia.maggstone.data.api.model

class CooperationHistoryRequest(val token: String, val ktrID : Int){
    val func: String = "listaDocumKtr"
}