package pl.tysia.maggstone.data.api.model

import pl.tysia.maggstone.constants.DocumentType
import pl.tysia.maggstone.data.model.DocumentItem

class NewDocumentRequest private constructor(val token : String,
                          val ktrID : Int?,
                          val magazID : Int?,
                          val docPozy : List<DocumentItem>){

    lateinit var docType : String
    val func : String = "addDocum"

    companion object{
        fun getNewOfferRequest( token : String,
                                ktrID : Int,
                                docPozy : List<DocumentItem>) : NewDocumentRequest{
            return NewDocumentRequest(token, ktrID, null, docPozy).apply { docType = DocumentType.OFFER }

        }

        fun getNewShiftRequest( token : String,
                                magazID : Int,
                                docPozy : List<DocumentItem>) : NewDocumentRequest{
            return NewDocumentRequest(token, null, magazID, docPozy).apply { docType = DocumentType.SHIFT }
        }
    }
}