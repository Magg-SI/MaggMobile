package pl.tysia.maggstone.data.source

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.preference.PreferenceManager
import pl.tysia.maggstone.constants.Preferences.LOGGED_USER_PREFERENCES
import pl.tysia.maggstone.constants.Preferences.PREF_ID
import pl.tysia.maggstone.constants.Preferences.PREF_MODE
import pl.tysia.maggstone.constants.Preferences.PREF_PROMPT_MODE
import pl.tysia.maggstone.constants.Preferences.PREF_TOKEN
import pl.tysia.maggstone.constants.Preferences.PREF_USERNAME
import pl.tysia.maggstone.data.Result
import pl.tysia.maggstone.data.model.LoggedInUser
import pl.tysia.maggstone.data.api.model.LoginResponse
import javax.inject.Inject


/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */


class LoginRepository @Inject constructor(val context: Context, var dataSource: LoginDataSource) {
    // in-memory cache of the loggedInUser object
    var user: LoggedInUser? = null
        private set

    var mode: String? = null
        private set

    var promptedDarkMode: String? = null
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

        val appPreferences = PreferenceManager
            .getDefaultSharedPreferences(context)

        mode = appPreferences.getString(PREF_MODE, null)
        promptedDarkMode = appPreferences.getString(PREF_PROMPT_MODE, null)

        user =
            if (username != null && token !=null && id > -1)
                LoggedInUser(id, username, token)
            else null

    }

    fun testToken() : Result<Boolean>{
        return dataSource.testToken()
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