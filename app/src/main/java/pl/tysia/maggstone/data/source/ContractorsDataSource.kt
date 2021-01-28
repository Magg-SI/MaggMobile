package pl.tysia.maggstone.data.source

import pl.tysia.maggstone.data.NetAddressManager
import pl.tysia.maggstone.data.Result
import pl.tysia.maggstone.data.api.model.APIResponse
import pl.tysia.maggstone.data.api.model.GetPageRequest
import pl.tysia.maggstone.data.api.model.PagesRequest
import pl.tysia.maggstone.data.api.service.PagesService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ContractorsDataSource(netAddressManager: NetAddressManager) : APISource(netAddressManager) {
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    fun getContractors(token: String, counter : Int) : Result<APIResponse.Pages> {
        val service = retrofit.create(PagesService::class.java)

        val result = service.pagesNumber(
            PagesRequest.Contractors(token = token, lastLicznik = counter)
        ).execute()

        return if (result.body()!!.retCode == APIResponse.RESPONSE_OK){
            Result.Success(result.body()!!)
        }else{
            Result.Error(Exception(result.body()!!.retMessage))
        }
    }

    fun getContractorsPage(token: String, listID : Int, pageNumber : Int) : Result<APIResponse.ContractorsPage> {
        val service = retrofit.create(PagesService::class.java)

        val result = service.getContractorsPage(
            GetPageRequest(token = token, listaID = listID, pageNo = pageNumber)
        ).execute()

        return if (result.body()!!.retCode == APIResponse.RESPONSE_OK){
            Result.Success(result.body()!!)
        }else{
            Result.Error(Exception(result.body()!!.retMessage))
        }
    }
}