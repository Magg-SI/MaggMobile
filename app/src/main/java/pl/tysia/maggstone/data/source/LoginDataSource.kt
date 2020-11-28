package pl.tysia.maggstone.data.source

import pl.tysia.maggstone.data.Result
import pl.tysia.maggstone.data.model.LoggedInUser
import pl.tysia.maggstone.data.api.model.LoginRequest
import pl.tysia.maggstone.data.api.model.LoginResponse
import pl.tysia.maggstone.data.api.service.LoginService
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
private const val BASE_URL = "http://martech.magg.pl/"

class LoginDataSource {

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

    fun login(username: String, password: String): Result<LoggedInUser> {
        return try {
            // TODO: handle loggedInUser authentication
            val fakeUser = LoggedInUser(java.util.UUID.randomUUID().variant(), "Jane Doe", "token")
            Result.Success(fakeUser)
        } catch (e: Throwable) {
            Result.Error(
                IOException(
                    "Error logging in",
                    e
                )
            )
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}