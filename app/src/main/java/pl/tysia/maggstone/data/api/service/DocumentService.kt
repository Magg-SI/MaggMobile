package pl.tysia.maggstone.data.api.service

import pl.tysia.maggstone.data.api.model.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface DocumentService {
    @POST("json.aspx")
    fun sendDocument(@Body body: NewDocumentRequest): Call<APIResponse.Document>

    @POST("json.aspx")
    fun testWorker(@Body body: APIRequest.TestWorker): Call<APIResponse.Worker>

    @POST("json.aspx")
    fun getWarePrice(@Body body: APIRequest.GetWarePrice): Call<APIResponse.WarePrice>

    @POST("json.aspx")
    fun getStocktakingWares(@Body body: APIRequest.GetStocktakingWares): Call<APIResponse.Stocktaking>

    @POST("json.aspx")
    fun addStocktakingWares(@Body body: APIRequest.AddStocktakingWare): Call<APIResponse>

    @POST("json.aspx")
    fun updateStocktakingWares(@Body body: APIRequest.UpdateStocktakingWare): Call<APIResponse>

    @POST("json.aspx")
    fun testStocktakingPosition(@Body body: APIRequest.TestStocktakingPosition): Call<APIResponse.TestStocktakingPosition>
}