package pl.tysia.maggstone.data.source

import android.content.Context
import android.content.Context.MODE_PRIVATE
import pl.tysia.maggstone.data.Result
import pl.tysia.maggstone.data.model.LoggedInUser
import pl.tysia.maggstone.data.api.model.LoginResponse


/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

private const val LOGGED_USER_PREFERENCES = "pl.tysia.maggstone.logged_user_prefs"

private const val PREF_USERNAME = "pl.tysia.maggstone.username"
private const val PREF_TOKEN = "pl.tysia.maggstone.token"
private const val PREF_ID = "pl.tysia.maggstone.id"

class LoginRepository(val dataSource: LoginDataSource, val context: Context) {

    // in-memory cache of the loggedInUser object
    var user: LoggedInUser? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore

        val preferences = context.getSharedPreferences(LOGGED_USER_PREFERENCES, MODE_PRIVATE)
        val username = preferences.getString(PREF_USERNAME, null)
        val id = preferences.getInt(PREF_ID, -1)
        val token =preferences.getString(PREF_TOKEN, null)

        user =
            if (username != null && token !=null && id > -1)
                LoggedInUser(id, username, token)
            else null

    }

    fun testToken(token : String) : Result<Boolean>{
        return dataSource.testToken(token)
    }

    fun logout() {
        val preferences = context.getSharedPreferences(LOGGED_USER_PREFERENCES, MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString(PREF_USERNAME, null)
        editor.putInt(PREF_ID, -1)
        editor.putString(PREF_TOKEN, null)
        editor.apply()
        user = null
        dataSource.logout()
    }

    fun login(username: String, password: String): Result<LoggedInUser> {
        // handle login

        val response= dataSource.loginCall(username, password).execute()

        val loginResponse = response.body()!!
        var result : Result<LoggedInUser>? = null

        if (loginResponse.retCode == LoginResponse.LOGIN_OK) {
            result = Result.Success(
                LoggedInUser(
                    loginResponse.userID!!,
                    username,
                    loginResponse.token!!
                )
            )
            setLoggedInUser(result.data)
        }else{
            result =
                Result.Error(Exception(loginResponse.retMessage))
        }

        return result
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser

        val preferences = context.getSharedPreferences(LOGGED_USER_PREFERENCES, MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString(PREF_USERNAME, loggedInUser.displayName)
        editor.putInt(PREF_ID, loggedInUser.userId)
        editor.putString(PREF_TOKEN, loggedInUser.token)
        editor.apply()

        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}