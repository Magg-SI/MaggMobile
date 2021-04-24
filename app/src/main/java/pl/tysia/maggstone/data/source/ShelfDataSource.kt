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
import javax.inject.Inject

class ShelfDataSource @Inject constructor(val retrofit: Retrofit, val tokenProvider: TokenProvider) {
    fun testShelf(qrCode: String) : Result<Boolean> {
        val service = retrofit.create(ShelfService::class.java)

        val result = service.testShelf(
            TestShelfRequest(token = tokenProvider.getToken()!!, localization = qrCode)
        ).execute()

        return if (result.body()!!.retCode == APIResponse.RESPONSE_OK){
            Result.Success(true)
        }else{
            Result.Error(Exception(result.body()!!.retMessage))
        }
    }

    fun addToShelf(wareQr: String, shelfQr : String) : Result<String> {
        val service = retrofit.create(ShelfService::class.java)

        val result = service.addToShelf(
            ChangeWareLocationRequest(token = tokenProvider.getToken()!!, localization = shelfQr, towQR = wareQr)
        ).execute()

        return if (result.body()!!.retCode == APIResponse.RESPONSE_OK){
            Result.Success(result.body()!!.index!!)
        }else{
            Result.Error(Exception(result.body()!!.retMessage))
        }
    }
}