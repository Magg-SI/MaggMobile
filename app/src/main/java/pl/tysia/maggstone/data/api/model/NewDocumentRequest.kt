package pl.tysia.maggstone.data.api.model

import pl.tysia.maggstone.data.model.DocumentItem

class NewDocumentRequest (val token : String,
                          val ktrID : Int,
                          val docPozy : List<DocumentItem>){
    val docType : String = "13-01"
    val func : String = "addDocum"
}