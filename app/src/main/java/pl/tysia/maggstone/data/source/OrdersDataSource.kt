package pl.tysia.maggstone.data.source

import pl.tysia.maggstone.data.NetAddressManager
import pl.tysia.maggstone.data.Result
import pl.tysia.maggstone.data.api.model.*
import pl.tysia.maggstone.data.api.service.OrdersService
import pl.tysia.maggstone.data.model.Order
import pl.tysia.maggstone.data.model.OrderedWare
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import javax.inject.Inject


class OrdersDataSource @Inject constructor(val retrofit: Retrofit, val tokenProvider: TokenProvider)  {
    fun getOrders() : Result<ArrayList<Order>> {
        val service = retrofit.create(OrdersService::class.java)

        val result = service.getOrders(
            GetOrdersRequest(tokenProvider.getToken()!!)
        ).execute()

        return if (result.body()!!.retCode == APIResponse.RESPONSE_OK){
            Result.Success(result.body()!!.orders!!)
        }else{
            Result.Error(Exception(result.body()!!.retMessage))
        }
    }

    fun getOrder(id : Int): Result<ArrayList<OrderedWare>> {
        val service = retrofit.create(OrdersService::class.java)

        val result = service.getOrder(
            GetOrderRequest(tokenProvider.getToken()!!, id)
        ).execute()

        return if (result.body()!!.retCode == APIResponse.RESPONSE_OK){
            Result.Success(result.body()!!.wares!!)
        }else{
            Result.Error(Exception(result.body()!!.retMessage))
        }
    }

    fun packWare(id : Int,
                 packedNumber : Double,
                 postponedNumber : Double,
                 cancelledNumber : Double): Result<Boolean> {

     val service = retrofit.create(OrdersService::class.java)

        val result = service.packWare(
            PackWareRequest(token = tokenProvider.getToken()!!,
                id = id,
                packedNumber = packedNumber,
                postponedNumber = postponedNumber,
                cancelledNumber = cancelledNumber
            )
        ).execute()

        return if (result.body()!!.retCode == APIResponse.RESPONSE_OK){
            Result.Success(true)
        }else{
            Result.Error(Exception(result.body()!!.retMessage))
        }

    }


}