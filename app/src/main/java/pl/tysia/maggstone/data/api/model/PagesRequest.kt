package pl.tysia.maggstone.data.api.model

sealed class PagesRequest( val func : String,
                           val token: String,
                           val lastLicznik: Int) {


    class Wares(token: String, lastLicznik: Int)
        : PagesRequest("listaTowarow", token, lastLicznik)

    class Contractors(token: String, lastLicznik: Int)
        : PagesRequest("listaKontrah", token, lastLicznik)
}