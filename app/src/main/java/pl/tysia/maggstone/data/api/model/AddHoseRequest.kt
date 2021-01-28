package pl.tysia.maggstone.data.api.model



class AddHoseRequest (val token: String,
                      val kodWeza: String,
                      val ktoWykonal: String,
                      val katSkrecenia: String,
                      val pozy : List<RequestItem>){
    val func = "addWaz"

    companion object{
        const val TIP_QUANTITY = 1
        const val SLEEVE_QUANTITY = 2
    }

    data class RequestItem(val typ: String, val towID: Int, val ilosc: Int)
}