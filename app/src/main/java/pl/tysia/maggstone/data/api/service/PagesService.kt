package pl.tysia.maggstone.data.api.service

import pl.tysia.maggstone.data.api.model.APIResponse
import pl.tysia.maggstone.data.api.model.GetPageRequest
import pl.tysia.maggstone.data.api.model.PagesRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface PagesService {
    @POST("json.aspx")
    fun pagesNumber(@Body body: PagesRequest): Call<APIResponse.Pages>

    @POST("json.aspx")
    fun getWaresPage(@Body body : GetPageRequest) : Call<APIResponse.WaresPage>

    @POST("json.aspx")
    fun getContractorsPage(@Body body : GetPageRequest) : Call<APIResponse.ContractorsPage>
}