package pl.tysia.maggstone.data.api.service

import pl.tysia.maggstone.data.api.model.LoginRequest
import pl.tysia.maggstone.data.api.model.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface DocumentService {
    @POST("json.aspx")
    fun sendDocument(@Body body: LoginRequest): Call<LoginResponse>
}