package pl.tysia.maggstone.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import pl.tysia.maggstone.constants.Preferences
import pl.tysia.maggstone.constants.UserTypes
import java.io.Serializable

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
class LoggedInUser(
    val userId: Int,
    val displayName: String,
    val token: String,
    val userTypeInt : Int
) : Serializable {

   fun getUserType() : UserTypes = UserTypes.fromInt(userTypeInt)

}