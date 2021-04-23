package pl.tysia.maggstone.data.source

import pl.tysia.maggstone.data.NetAddressManager
import pl.tysia.maggstone.data.Result
import pl.tysia.maggstone.data.api.model.*
import pl.tysia.maggstone.data.api.service.OrdersService
import pl.tysia.maggstone.data.api.service.TechniciansService
import pl.tysia.maggstone.data.api.service.WarehouseService
import pl.tysia.maggstone.data.model.Order
import pl.tysia.maggstone.data.model.OrderedWare
import pl.tysia.maggstone.data.model.Technician
import pl.tysia.maggstone.data.model.Warehouse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception


class TechniciansDataSource(netAddressManager: NetAddressManager) : APISource(netAddressManager) {
    fun getTechnicians(token : String) : Result<ArrayList<Technician>> {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(TechniciansService::class.java)


        val result = service.getTechnicians(
            GetTechniciansRequest(token)
        ).execute()

        return if (result.body()!!.retCode == APIResponse.RESPONSE_OK){
            Result.Success(result.body()!!.technicians!!)
        }else{
            Result.Error(Exception(result.body()!!.retMessage))
        }
    }


}