package pl.tysia.maggstone.data.source

import pl.tysia.maggstone.data.NetAddressManager
import pl.tysia.maggstone.data.Result
import pl.tysia.maggstone.data.api.model.APIResponse
import pl.tysia.maggstone.data.api.model.ChangeWareLocationRequest
import pl.tysia.maggstone.data.api.model.FindWareRequest
import pl.tysia.maggstone.data.api.model.TestShelfRequest
import pl.tysia.maggstone.data.api.service.ShelfService
import pl.tysia.maggstone.data.api.service.WareService
import pl.tysia.maggstone.data.model.Ware
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ShelfDataSource(netAddressManager: NetAddressManager) : APISource(netAddressManager) {
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun testShelf(qrCode: String, token: String) : Result<Boolean> {
        val service = retrofit.create(ShelfService::class.java)

        val result = service.testShelf(
            TestShelfRequest(token = token, localization = qrCode)
        ).execute()

        return if (result.body()!!.retCode == APIResponse.RESPONSE_OK){
            Result.Success(true)
        }else{
            Result.Error(Exception(result.body()!!.retMessage))
        }
    }

    fun addToShelf(wareQr: String, shelfQr : String, token: String) : Result<Boolean> {
        val service = retrofit.create(ShelfService::class.java)

        val result = service.addToShelf(
            ChangeWareLocationRequest(token = token, localization = shelfQr, towQR = wareQr)
        ).execute()

        return if (result.body()!!.retCode == APIResponse.RESPONSE_OK){
            Result.Success(true)
        }else{
            Result.Error(Exception(result.body()!!.retMessage))
        }
    }
}