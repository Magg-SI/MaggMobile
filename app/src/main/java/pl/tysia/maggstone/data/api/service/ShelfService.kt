package pl.tysia.maggstone.data.api.service

import pl.tysia.maggstone.data.api.model.APIResponse
import pl.tysia.maggstone.data.api.model.ChangeWareLocationRequest
import pl.tysia.maggstone.data.api.model.TestShelfRequest
import pl.tysia.maggstone.data.api.model.UpdatePictureRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ShelfService {

    @POST("json.aspx")
    fun testShelf(@Body body : TestShelfRequest) : Call<APIResponse>


    @POST("json.aspx")
    fun addToShelf(@Body body : ChangeWareLocationRequest) : Call<APIResponse>
}