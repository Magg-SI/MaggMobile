package pl.tysia.maggstone.data.api.service

import pl.tysia.maggstone.data.api.model.*
import pl.tysia.maggstone.data.model.Ware
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface WareService {
    @POST("json.aspx")
    fun findBy(@Body body : FindWareRequest) : Call<Ware>

    @POST("json.aspx")
    fun updatePicture(@Body body : UpdatePictureRequest) : Call<APIResponse>

    @POST("json.aspx")
    fun getPicture(@Body body : GetPictureRequest) : Call<APIResponse.Picture>

    @POST("json.aspx")
    fun getAvailabilities(@Body body : GetAvailabilitiesRequest) : Call<APIResponse.Availabilities>

}