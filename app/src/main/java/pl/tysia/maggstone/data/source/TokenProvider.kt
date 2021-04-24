package pl.tysia.maggstone.data.source

import android.content.Context
import pl.tysia.maggstone.constants.Preferences.LOGGED_USER_PREFERENCES
import pl.tysia.maggstone.constants.Preferences.PREF_TOKEN
import javax.inject.Inject

class TokenProvider @Inject constructor(val context: Context) {

    fun getToken() : String?{
        val preferences = context.getSharedPreferences(LOGGED_USER_PREFERENCES,
            Context.MODE_PRIVATE
        )

        return preferences.getString(PREF_TOKEN, null)
    }
}