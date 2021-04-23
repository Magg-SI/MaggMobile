package pl.tysia.maggstone.data.api.service

import pl.tysia.maggstone.data.api.model.APIResponse
import pl.tysia.maggstone.data.api.model.GetTechniciansRequest
import pl.tysia.maggstone.data.api.model.GetWarehousesRequest
import pl.tysia.maggstone.data.api.model.UpdatePictureRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface TechniciansService {
    @POST("json.aspx")
    fun getTechnicians(@Body body : GetTechniciansRequest) : Call<APIResponse.Technicians>
}