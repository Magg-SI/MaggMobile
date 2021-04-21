package pl.tysia.maggstone.data.api.model

import com.google.gson.annotations.SerializedName
import pl.tysia.maggstone.constants.DocumentType
import pl.tysia.maggstone.data.model.DocumentItem
import java.io.Serializable

class NewDocumentRequest private constructor(val token : String,
                                             val ktrID : Int?,
                                             val magazID : Int?,
                                             val docPozy : List<DocumentItem>) : Serializable{

    lateinit var docType : String
    val func : String = "addDocum"

    var uwagi : String? = null
    var podpis : String? = null

    companion object{
        fun getNewOfferRequest( token : String,
                                ktrID : Int,
                                podpis : String,
                                uwagi : String,
                                docPozy : List<DocumentItem>) : NewDocumentRequest{
            return NewDocumentRequest(token, ktrID, null, docPozy).apply {
                docType = DocumentType.OFFER
                this.podpis = podpis
                this.uwagi = uwagi
            }
        }

        fun getNewShiftRequest( token : String,
                                magazID : Int,
                                podpis : String,
                                uwagi : String,
                                docPozy : List<DocumentItem>) : NewDocumentRequest{
            return NewDocumentRequest(token, null, magazID, docPozy).apply {
                docType = DocumentType.SHIFT
                this.podpis = podpis
                this.uwagi = uwagi
            }
        }
    }
}