package pl.tysia.maggstone.data.api.service

import pl.tysia.maggstone.data.api.model.APIResponse
import pl.tysia.maggstone.data.api.model.LoginRequest
import pl.tysia.maggstone.data.api.model.LoginResponse
import pl.tysia.maggstone.data.api.model.NewDocumentRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface DocumentService {
    @POST("json.aspx")
    fun sendDocument(@Body body: NewDocumentRequest): Call<APIResponse>
}