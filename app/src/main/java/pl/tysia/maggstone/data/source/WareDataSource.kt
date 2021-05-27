package pl.tysia.maggstone.data.source

import android.graphics.Bitmap
import pl.tysia.maggstone.data.NetAddressManager
import pl.tysia.maggstone.data.Result
import pl.tysia.maggstone.data.api.model.*
import pl.tysia.maggstone.data.api.service.PagesService
import pl.tysia.maggstone.data.api.service.WareService
import pl.tysia.maggstone.data.model.Hose
import pl.tysia.maggstone.data.model.Ware
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject


class WareDataSource @Inject constructor(val retrofit: Retrofit, val tokenProvider: TokenProvider)  {
    fun findHose( hoseNumber : String) : Result<Hose>{
        val service = retrofit.create(WareService::class.java)

        val result = service.getHose(
            GetHoseRequest(token = tokenProvider.getToken()!!, numer = hoseNumber)
        ).execute()

        return if (result.body()!!.retCode == APIResponse.RESPONSE_OK){
            Result.Success(result.body()!!)
        }else{
            Result.Error(Exception(result.body()!!.retMessage))
        }
    }

    fun orderWare(id : Int, number : Double, comments : String) : Result<Boolean> {
        val service = retrofit.create(WareService::class.java)

        val result = service.orderWare(
            APIRequest.OrderWare(id, number, comments, tokenProvider.getToken()!!)
        ).execute()

        return if (result.body()!!.retCode == APIResponse.RESPONSE_OK){
            Result.Success(true)
        }else{
            Result.Error(Exception(result.body()!!.retMessage))
        }
    }

    fun getWare(qrCode: String) : Result<Ware> {
        val service = retrofit.create(WareService::class.java)

        val result = service.findBy(
            FindWareRequest(token = tokenProvider.getToken()!!, keyCode = FindWareRequest.KEY_QR, keyValue = qrCode)
        ).execute()

        return if (result.body()!!.retCode == APIResponse.RESPONSE_OK){
            Result.Success(result.body()!!)
        }else{
            Result.Error(Exception(result.body()!!.retMessage))
        }
    }

    fun addHose(hose: Hose) : Result<Hose> {
        val service = retrofit.create(WareService::class.java)

        val items = ArrayList<AddHoseRequest.RequestItem>()

        items.add(AddHoseRequest.RequestItem(hose.cord!!.hoseType!!, hose.cord!!.id!!, hose.length!!))
        items.add(AddHoseRequest.RequestItem(hose.tip1!!.hoseType!!, hose.tip1!!.id!!, AddHoseRequest.TIP_QUANTITY))
        items.add(AddHoseRequest.RequestItem(hose.tip2!!.hoseType!!, hose.tip2!!.id!!, AddHoseRequest.TIP_QUANTITY))
        items.add(AddHoseRequest.RequestItem(hose.sleeve!!.hoseType!!, hose.sleeve!!.id!!, AddHoseRequest.SLEEVE_QUANTITY))

        val result = service.addHose(
            AddHoseRequest(tokenProvider.getToken()!!, hose.code!!, hose.creator!!, hose.angle!!, items)
        ).execute()

        return if (result.body()!!.retCode == APIResponse.RESPONSE_OK){
            val hoseResult = result.body()
            hose.name = hoseResult!!.nazwa
            hose.id = hoseResult.wazID
            hose.priceN = hoseResult.cenaN
            hose.priceB = hoseResult.cenaB
            Result.Success(hose)
        }else{
            Result.Error(Exception(result.body()!!.retMessage))
        }
    }

    fun getAvailabilities(index: String) : Result<APIResponse.Availabilities> {
        val service = retrofit.create(WareService::class.java)

        val result = service.getAvailabilities(
            GetAvailabilitiesRequest(tokenProvider.getToken()!!, index)
        ).execute()

        return if (result.body()!!.retCode == APIResponse.RESPONSE_OK){
            Result.Success(result.body()!!)
        }else{
            Result.Error(Exception(result.body()!!.retMessage))
        }
    }

    fun getWares(counter : Int) : Result<APIResponse.Pages> {
        val service = retrofit.create(PagesService::class.java)

        val result = service.pagesNumber(
            PagesRequest.Wares(token = tokenProvider.getToken()!!, lastLicznik = counter)
        ).execute()

        return if (result.body()!!.retCode == APIResponse.RESPONSE_OK){
            Result.Success(result.body()!!)
        }else{
            Result.Error(Exception(result.body()!!.retMessage))
        }
    }

    fun getWaresPage(listID : Int, pageNumber : Int) : Result<APIResponse.WaresPage> {
        val service = retrofit.create(PagesService::class.java)

        val result = service.getWaresPage(
            GetPageRequest(token = tokenProvider.getToken()!!, listaID = listID, pageNo = pageNumber)
        ).execute()

        return if (result.body()!!.retCode == APIResponse.RESPONSE_OK){
            Result.Success(result.body()!!)
        }else{
            Result.Error(Exception(result.body()!!.retMessage))
        }
    }


}