package pl.tysia.maggstone.data.source

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import pl.tysia.maggstone.constants.Preferences.LOGGED_USER_PREFERENCES
import pl.tysia.maggstone.constants.Preferences.PREF_LOGGED_USER
import pl.tysia.maggstone.constants.Preferences.PREF_MODE
import pl.tysia.maggstone.constants.Preferences.PREF_PROMPT_MODE
import pl.tysia.maggstone.data.Database
import pl.tysia.maggstone.data.Result
import pl.tysia.maggstone.data.api.model.LoginResponse
import pl.tysia.maggstone.data.model.LoggedInUser
import javax.inject.Inject


/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */


class LoginRepository @Inject constructor(
    val context: Context,
    var dataSource: LoginDataSource,
    var db: Database
) {
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

        val appPreferences = PreferenceManager
            .getDefaultSharedPreferences(context)

        mode = appPreferences.getString(PREF_MODE, null)
        promptedDarkMode = appPreferences.getString(PREF_PROMPT_MODE, null)

        val preferences = context.getSharedPreferences(LOGGED_USER_PREFERENCES, MODE_PRIVATE)

        val gson = Gson()
        val json: String? = preferences.getString(PREF_LOGGED_USER, null)

        if (json != null){
            user = gson.fromJson(json, LoggedInUser::class.java)
        }
    }

    fun testToken() : Result<Boolean>{
        return dataSource.testToken(user!!.token)
    }

    fun logout() {
        val preferences = context.getSharedPreferences(LOGGED_USER_PREFERENCES, MODE_PRIVATE)
        preferences.edit().clear().apply()

        Thread {
            db.waresDao().deleteAll()
        }.start()

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
                    loginResponse.token!!,
                    loginResponse.userTypeInt!!
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

        val prefsEditor: SharedPreferences.Editor = preferences.edit()
        val gson = Gson()
        val json = gson.toJson(loggedInUser)
        prefsEditor.putString(PREF_LOGGED_USER, json)
        prefsEditor.apply()

        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}