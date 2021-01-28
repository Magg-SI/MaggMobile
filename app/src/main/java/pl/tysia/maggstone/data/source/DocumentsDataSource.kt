package pl.tysia.maggstone.data.source

import pl.tysia.maggstone.data.NetAddressManager
import pl.tysia.maggstone.data.Result
import pl.tysia.maggstone.data.api.model.APIResponse
import pl.tysia.maggstone.data.api.model.GetOrdersRequest
import pl.tysia.maggstone.data.api.model.NewDocumentRequest
import pl.tysia.maggstone.data.api.service.DocumentService
import pl.tysia.maggstone.data.api.service.OrdersService
import pl.tysia.maggstone.data.model.DocumentItem
import pl.tysia.maggstone.data.model.Order
import pl.tysia.maggstone.ui.presentation_logic.adapter.ICatalogable
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception


class DocumentsDataSource(netAddressManager: NetAddressManager) : APISource(netAddressManager) {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: DocumentService = retrofit.create(DocumentService::class.java)


    fun sendDocument(token : String, ktrID : Int, items : List<DocumentItem>) : Result<Int> {

        val result = service.sendDocument(
            NewDocumentRequest(token, ktrID, items)
        ).execute()

        return if (result.body()!!.retCode == APIResponse.RESPONSE_OK){
            Result.Success(result.body()!!.retCode!!)
        }else{
            Result.Error(Exception(result.body()!!.retMessage))
        }
    }

}