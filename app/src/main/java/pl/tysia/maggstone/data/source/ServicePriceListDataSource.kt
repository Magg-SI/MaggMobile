package pl.tysia.maggstone.data.source

import pl.tysia.maggstone.data.Result
import pl.tysia.maggstone.data.api.model.*
import pl.tysia.maggstone.data.api.service.ServiceService
import pl.tysia.maggstone.data.model.ServicePrice
import retrofit2.Retrofit
import java.lang.Exception
import javax.inject.Inject


class ServicePriceListDataSource @Inject constructor(val retrofit: Retrofit, val tokenProvider: TokenProvider)  {
    fun getPriceList() : Result<ArrayList<ServicePrice>> {
        val service = retrofit.create(ServiceService::class.java)

        val result = service.getPriceList(
            GetServicePricesRequest(tokenProvider.getToken()!!)
        ).execute()

        return if (result.body()!!.retCode == APIResponse.RESPONSE_OK){
            Result.Success(result.body()!!.priceList!!)
        }else{
            Result.Error(Exception(result.body()!!.retMessage))
        }
    }
}