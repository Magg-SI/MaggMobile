package pl.tysia.maggstone.data.api.service

import pl.tysia.maggstone.data.api.model.APIResponse
import pl.tysia.maggstone.data.api.model.GetTechniciansRequest
import pl.tysia.maggstone.data.api.model.GetWarehousesRequest
import pl.tysia.maggstone.data.api.model.UpdatePictureRequest
import pl.tysia.maggstone.data.model.Service
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ServiceService {
    @POST("json.aspx")
    fun addService(@Body body : Service) : Call<APIResponse.Service>
}