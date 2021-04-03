package pl.tysia.maggstone.data.api.service

import pl.tysia.maggstone.data.api.model.APIResponse
import pl.tysia.maggstone.data.api.model.GetWarehousesRequest
import pl.tysia.maggstone.data.api.model.UpdatePictureRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface WarehouseService {
    @POST("json.aspx")
    fun getWarehouses(@Body body : GetWarehousesRequest) : Call<APIResponse.Warehouses>
}