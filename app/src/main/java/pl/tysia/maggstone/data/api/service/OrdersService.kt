package pl.tysia.maggstone.data.api.service

import pl.tysia.maggstone.data.api.model.*
import pl.tysia.maggstone.data.model.OrderedWare
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface OrdersService {

    @POST("json.aspx")
    fun getOrders(@Body body: GetOrdersRequest): Call<APIResponse.GetOrders>

    @POST("json.aspx")
    fun getOrder(@Body body: GetOrderRequest): Call<APIResponse.GetOrderedWares>

    @POST("json.aspx")
    fun packWare(@Body body: PackWareRequest): Call<APIResponse>

}