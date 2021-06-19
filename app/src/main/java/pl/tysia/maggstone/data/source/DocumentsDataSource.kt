package pl.tysia.maggstone.data.source

import pl.tysia.maggstone.data.NetAddressManager
import pl.tysia.maggstone.data.Result
import pl.tysia.maggstone.data.api.model.APIRequest
import pl.tysia.maggstone.data.api.model.APIResponse
import pl.tysia.maggstone.data.api.model.NewDocumentRequest
import pl.tysia.maggstone.data.api.service.DocumentService
import pl.tysia.maggstone.data.model.DocumentItem
import pl.tysia.maggstone.data.model.Ware
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import javax.inject.Inject


class DocumentsDataSource @Inject constructor(val retrofit: Retrofit, val tokenProvider: TokenProvider)  {
    val service: DocumentService = retrofit.create(DocumentService::class.java)

    fun testWorker(workerID : Int) : APIResponse.Worker {

        val result = service.testWorker(
            APIRequest.TestWorker(workerID, tokenProvider.getToken()!!)
        ).execute()

        return result.body()!!
    }

    fun getWarePrice(ware : Ware, ktrID : Int) : Result<Ware> {

        val result = service.getWarePrice(
            APIRequest.GetWarePrice(ware.id!!, ktrID, tokenProvider.getToken()!!)
        ).execute()

        return if (result.body()!!.retCode == APIResponse.RESPONSE_OK){
            ware.priceN=result.body()!!.cena!!
            Result.Success(ware)
        }else{
            Result.Error(Exception(result.body()!!.retMessage))
        }
    }


    fun sendDocument(ktrID : Int, sign : String, comments : String, items : List<DocumentItem>) : Result<Int> {

        val result = service.sendDocument(
            NewDocumentRequest.getNewOfferRequest(tokenProvider.getToken()!!, ktrID, sign, comments, items)
        ).execute()

        return if (result.body()!!.retCode == APIResponse.RESPONSE_OK){
            Result.Success(result.body()!!.workerID!!)
        }else{
            Result.Error(Exception(result.body()!!.retMessage))
        }
    }

    fun sendShiftDocument(id : Int, sign : String, comments : String, items : List<DocumentItem>) : Result<Int> {

        val result = service.sendDocument(
            NewDocumentRequest.getNewShiftRequest(tokenProvider.getToken()!!, id, sign, comments, items)
        ).execute()

        return if (result.body()!!.retCode == APIResponse.RESPONSE_OK){
            Result.Success(result.body()!!.workerID!!)
        }else{
            Result.Error(Exception(result.body()!!.retMessage))
        }
    }

}