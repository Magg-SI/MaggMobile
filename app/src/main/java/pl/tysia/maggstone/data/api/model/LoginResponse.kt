package pl.tysia.maggstone.data.api.model

import com.google.gson.annotations.SerializedName

class LoginResponse : APIResponse() {
    companion object {
        const val LOGIN_OK = 0
        const val ERR_USERNAME = 1
        const val ERR_PASSWORD = 2
    }

    @SerializedName("userID")
    var userID: Int? = null

    @SerializedName("token")
    var token: String? = null
}