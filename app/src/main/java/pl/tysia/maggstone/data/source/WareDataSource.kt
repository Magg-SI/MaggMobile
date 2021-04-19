package pl.tysia.maggstone.data.source

import pl.tysia.maggstone.data.NetAddressManager
import pl.tysia.maggstone.data.Result
import pl.tysia.maggstone.data.api.model.*
import pl.tysia.maggstone.data.api.service.PagesService
import pl.tysia.maggstone.data.api.service.WareService
import pl.tysia.maggstone.data.model.Hose
import pl.tysia.maggstone.data.model.Ware
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class WareDataSource(netAddressManager: NetAddressManager) : APISource(netAddressManager) {
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun findHose(token : String, hoseNumber : String) : Result<Hose>{
        val service = retrofit.create(WareService::class.java)

        val result = service.getHose(
            GetHoseRequest(token = token, numer = hoseNumber)
        ).execute()

        return if (result.body()!!.retCode == APIResponse.RESPONSE_OK){
            Result.Success(result.body()!!)
        }else{
            Result.Error(Exception(result.body()!!.retMessage))
        }
    }

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

    fun addHose(hose: Hose, token: String) : Result<Hose> {
        val service = retrofit.create(WareService::class.java)

        val items = ArrayList<AddHoseRequest.RequestItem>()

        items.add(AddHoseRequest.RequestItem(hose.cord!!.hoseType!!, hose.cord!!.id!!, hose.length!!))
        items.add(AddHoseRequest.RequestItem(hose.tip1!!.hoseType!!, hose.tip1!!.id!!, AddHoseRequest.TIP_QUANTITY))
        items.add(AddHoseRequest.RequestItem(hose.tip2!!.hoseType!!, hose.tip2!!.id!!, AddHoseRequest.TIP_QUANTITY))
        items.add(AddHoseRequest.RequestItem(hose.sleeve!!.hoseType!!, hose.sleeve!!.id!!, AddHoseRequest.SLEEVE_QUANTITY))

        val result = service.addHose(
            AddHoseRequest(token, hose.code!!, hose.creator!!, hose.angle!!, items)
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