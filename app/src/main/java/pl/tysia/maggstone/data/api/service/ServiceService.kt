package pl.tysia.maggstone.data.api.service

import pl.tysia.maggstone.data.api.model.*
import pl.tysia.maggstone.data.model.Service
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ServiceService {
    @POST("json.aspx")
    fun addService(@Body body : Service) : Call<APIResponse.Service>

    @POST("json.aspx")
    fun getPriceList(@Body body: GetServicePricesRequest): Call<APIResponse.GetPriceList>

}