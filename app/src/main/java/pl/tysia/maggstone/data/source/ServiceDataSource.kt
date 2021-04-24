package pl.tysia.maggstone.data.source

import pl.tysia.maggstone.data.NetAddressManager
import pl.tysia.maggstone.data.Result
import pl.tysia.maggstone.data.api.model.*
import pl.tysia.maggstone.data.api.service.OrdersService
import pl.tysia.maggstone.data.api.service.ServiceService
import pl.tysia.maggstone.data.api.service.TechniciansService
import pl.tysia.maggstone.data.api.service.WarehouseService
import pl.tysia.maggstone.data.model.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import javax.inject.Inject


class ServiceDataSource @Inject constructor(val retrofit: Retrofit, val tokenProvider: TokenProvider)  {
    fun addService(service : Service) : Result<Service> {
       val retrofitService = retrofit.create(ServiceService::class.java)

        service.token = tokenProvider.getToken()!!

        val result = retrofitService.addService(
            service
        ).execute()

        return if (result.body()!!.retCode == APIResponse.RESPONSE_OK){
            val apiResponse = result.body()
            service.name = apiResponse!!.name
            service.priceB = apiResponse!!.priceB
            service.priceN = apiResponse!!.priceN
            service.id = apiResponse!!.id

            Result.Success(service)
        }else{
            Result.Error(Exception(result.body()!!.retMessage))
        }
    }


}