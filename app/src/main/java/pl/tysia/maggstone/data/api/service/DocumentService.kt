package pl.tysia.maggstone.data.api.service

import pl.tysia.maggstone.data.api.model.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface DocumentService {
    @POST("json.aspx")
    fun sendDocument(@Body body: NewDocumentRequest): Call<APIResponse.Document>

    @POST("json.aspx")
    fun testWorker(@Body body: APIRequest.TestWorker): Call<APIResponse.Worker>
}