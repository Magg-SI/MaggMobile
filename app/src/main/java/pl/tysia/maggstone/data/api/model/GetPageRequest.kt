package pl.tysia.maggstone.data.api.model

class GetPageRequest(val func: String = "getListaPage",
                            val token: String,
                            val listaID: Int,
                            val pageNo: Int)