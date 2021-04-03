package pl.tysia.maggstone.data.source

import pl.tysia.maggstone.data.NetAddressManager
import pl.tysia.maggstone.data.Result
import pl.tysia.maggstone.data.api.model.*
import pl.tysia.maggstone.data.api.service.OrdersService
import pl.tysia.maggstone.data.api.service.WarehouseService
import pl.tysia.maggstone.data.model.Order
import pl.tysia.maggstone.data.model.OrderedWare
import pl.tysia.maggstone.data.model.Warehouse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception


class WarehousesDataSource(netAddressManager: NetAddressManager) : APISource(netAddressManager) {
    fun getWarehouses(token : String) : Result<ArrayList<Warehouse>> {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(WarehouseService::class.java)


        val result = service.getWarehouses(
            GetWarehousesRequest(token)
        ).execute()

        return if (result.body()!!.retCode == APIResponse.RESPONSE_OK){
            Result.Success(result.body()!!.warehouses!!)
        }else{
            Result.Error(Exception(result.body()!!.retMessage))
        }
    }


}