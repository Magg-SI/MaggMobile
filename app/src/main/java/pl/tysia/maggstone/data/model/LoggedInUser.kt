package pl.tysia.maggstone.data.model

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class LoggedInUser(
    val userId: Int,
    val displayName: String,
    val token: String
)