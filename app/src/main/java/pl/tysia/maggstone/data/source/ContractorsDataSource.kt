package pl.tysia.maggstone.data.source

import pl.tysia.maggstone.data.NetAddressManager
import pl.tysia.maggstone.data.Result
import pl.tysia.maggstone.data.api.model.APIResponse
import pl.tysia.maggstone.data.api.model.CooperationHistoryRequest
import pl.tysia.maggstone.data.api.model.GetPageRequest
import pl.tysia.maggstone.data.api.model.PagesRequest
import pl.tysia.maggstone.data.api.service.ContractorsService
import pl.tysia.maggstone.data.api.service.PagesService
import pl.tysia.maggstone.data.model.Document
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject


class ContractorsDataSource @Inject constructor(val retrofit: Retrofit, val tokenProvider: TokenProvider) {

    fun getContractors(counter : Int) : Result<APIResponse.Pages> {
        val service = retrofit.create(PagesService::class.java)

        val result = service.pagesNumber(
            PagesRequest.Contractors(token = tokenProvider.getToken()!!, lastLicznik = counter)
        ).execute()

        return if (result.body()!!.retCode == APIResponse.RESPONSE_OK){
            Result.Success(result.body()!!)
        }else{
            Result.Error(Exception(result.body()!!.retMessage))
        }
    }

    fun getCooperationHistory(id : Int) : Result<ArrayList<Document>> {
        val service = retrofit.create(ContractorsService::class.java)

        val result = service.getCooperationHistory(
            CooperationHistoryRequest(token = tokenProvider.getToken()!!, id)
        ).execute()

        return if (result.body()!!.retCode == APIResponse.RESPONSE_OK){
            Result.Success(result.body()!!.cooperationHistory!!)
        }else{
            Result.Error(Exception(result.body()!!.retMessage))
        }
    }

    fun getContractorsPage(listID : Int, pageNumber : Int) : Result<APIResponse.ContractorsPage> {
        val service = retrofit.create(PagesService::class.java)

        val result = service.getContractorsPage(
            GetPageRequest(token = tokenProvider.getToken()!!, listaID = listID, pageNo = pageNumber)
        ).execute()

        return if (result.body()!!.retCode == APIResponse.RESPONSE_OK){
            Result.Success(result.body()!!)
        }else{
            Result.Error(Exception(result.body()!!.retMessage))
        }
    }
}