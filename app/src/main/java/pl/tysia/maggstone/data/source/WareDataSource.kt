package pl.tysia.maggstone.data.source

import pl.tysia.maggstone.data.Result
import pl.tysia.maggstone.data.api.model.*
import pl.tysia.maggstone.data.api.service.PagesService
import pl.tysia.maggstone.data.api.service.WareService
import pl.tysia.maggstone.data.model.Ware
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "http://martech.magg.pl/"

class WareDataSource {
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    fun getWare(qrCode: String, token: String) : Result<Ware> {
        val service = retrofit.create(WareService::class.java)

        val result = service.findBy(
            FindWareRequest(token = token, keyCode = FindWareRequest.KEY_QR, keyValue = qrCode)
        ).execute()

        return if (result.body()!!.retCode == APIResponse.RESPONSE_OK){
            Result.Success(result.body()!!)
        }else{
            Result.Error(Exception(result.body()!!.retMessage))
        }
    }

    fun getAvailabilities(index: String, token: String) : Result<APIResponse.Availabilities> {
        val service = retrofit.create(WareService::class.java)

        val result = service.getAvailabilities(
            GetAvailabilitiesRequest(token, index)
        ).execute()

        return if (result.body()!!.retCode == APIResponse.RESPONSE_OK){
            Result.Success(result.body()!!)
        }else{
            Result.Error(Exception(result.body()!!.retMessage))
        }
    }

    fun getWares(token: String, counter : Int) : Result<APIResponse.Pages> {
        val service = retrofit.create(PagesService::class.java)

        val result = service.pagesNumber(
            PagesRequest.Wares(token = token, lastLicznik = counter)
        ).execute()

        return if (result.body()!!.retCode == APIResponse.RESPONSE_OK){
            Result.Success(result.body()!!)
        }else{
            Result.Error(Exception(result.body()!!.retMessage))
        }
    }

    fun getWaresPage(token: String, listID : Int, pageNumber : Int) : Result<APIResponse.WaresPage> {
        val service = retrofit.create(PagesService::class.java)

        val result = service.getWaresPage(
            GetPageRequest(token = token, listaID = listID, pageNo = pageNumber)
        ).execute()

        return if (result.body()!!.retCode == APIResponse.RESPONSE_OK){
            Result.Success(result.body()!!)
        }else{
            Result.Error(Exception(result.body()!!.retMessage))
        }
    }


}