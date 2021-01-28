package pl.tysia.maggstone.data.source

import pl.tysia.maggstone.data.NetAddressManager
import pl.tysia.maggstone.data.Result
import pl.tysia.maggstone.data.api.model.*
import pl.tysia.maggstone.data.model.LoggedInUser
import pl.tysia.maggstone.data.api.service.LoginService
import pl.tysia.maggstone.data.api.service.OrdersService
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.lang.Exception

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */

class LoginDataSource(netAddressManager: NetAddressManager) : APISource(netAddressManager) {

    fun loginCall(username: String, password: String) : Call<LoginResponse> {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(LoginService::class.java)


        return service.login(
            LoginRequest(
                login = username,
                password = password
            )
        )
    }

    fun testToken(token : String) : Result<Boolean>{
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(LoginService::class.java)


        val result = service.testToken(
           TestTokenRequest(token)
        ).execute()

        return if (result.body()!!.retCode == APIResponse.RESPONSE_OK){
            Result.Success(true)
        }else{
            Result.Error(Exception(result.body()!!.retMessage))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}