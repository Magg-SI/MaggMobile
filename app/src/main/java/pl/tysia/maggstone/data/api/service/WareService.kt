package pl.tysia.maggstone.data.api.service

import pl.tysia.maggstone.data.api.model.*
import pl.tysia.maggstone.data.model.Hose
import pl.tysia.maggstone.data.model.Ware
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface WareService {
    @POST("json.aspx")
    fun findBy(@Body body : FindWareRequest) : Call<Ware>

    @POST("json.aspx")
    fun updatePictureStart(@Body body : UpdatePictureRequest.Start) : Call<APIResponse.Photo>

    @POST("json.aspx")
    fun updatePictureNext(@Body body : UpdatePictureRequest.Next) : Call<APIResponse>

    @POST("json.aspx")
    fun updatePictureFin(@Body body : UpdatePictureRequest.Finalize) : Call<APIResponse>

    @POST("json.aspx")
    fun getPicture(@Body body : GetPictureRequest) : Call<APIResponse.Picture>

    @POST("json.aspx")
    fun getAvailabilities(@Body body : GetAvailabilitiesRequest) : Call<APIResponse.Availabilities>

    @POST("json.aspx")
    fun addHose(@Body body : AddHoseRequest) : Call<APIResponse.Hose>


    @POST("json.aspx")
    fun getHose(@Body body : GetHoseRequest) : Call<Hose>

}