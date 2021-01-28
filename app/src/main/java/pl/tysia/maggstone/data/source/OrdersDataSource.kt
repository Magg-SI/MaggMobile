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


class OrdersDataSource(netAddressManager: NetAddressManager) : APISource(netAddressManager) {
    fun getOrders(token : String) : Result<ArrayList<Order>> {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(OrdersService::class.java)


        val result = service.getOrders(
            GetOrdersRequest(token)
        ).execute()

        return if (result.body()!!.retCode == APIResponse.RESPONSE_OK){
            Result.Success(result.body()!!.orders!!)
        }else{
            Result.Error(Exception(result.body()!!.retMessage))
        }
    }

    fun getOrder(token : String, id : Int): Result<ArrayList<OrderedWare>> {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(OrdersService::class.java)


        val result = service.getOrder(
            GetOrderRequest(token, id)
        ).execute()

        return if (result.body()!!.retCode == APIResponse.RESPONSE_OK){
            Result.Success(result.body()!!.wares!!)
        }else{
            Result.Error(Exception(result.body()!!.retMessage))
        }
    }

    fun packWare(token : String,
                 id : Int,
                 packedNumber : Double,
                 postponedNumber : Double,
                 cancelledNumber : Double): Result<Boolean> {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(OrdersService::class.java)


        val result = service.packWare(
            PackWareRequest(token = token,
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