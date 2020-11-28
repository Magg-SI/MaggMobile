package pl.tysia.maggstone.data.api.service

import pl.tysia.maggstone.data.api.model.LoginRequest
import pl.tysia.maggstone.data.api.model.LoginResponse
import retrofit2.Call
import retrofit2.http.*

interface LoginService {
    @POST("json.aspx")
    fun login(@Body body: LoginRequest): Call<LoginResponse>

}