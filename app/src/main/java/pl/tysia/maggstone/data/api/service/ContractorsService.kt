package pl.tysia.maggstone.data.api.service

import pl.tysia.maggstone.data.api.model.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ContractorsService {
    @POST("json.aspx")
    fun getCooperationHistory(@Body body: CooperationHistoryRequest): Call<APIResponse.CooperationHistory>
}