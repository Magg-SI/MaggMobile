package pl.tysia.maggstone.data.api.model

data class ChangeWareLocationRequest(val token: String,
                                    val localization: String,
                                    val towQR: String) {
    val func = "addPlace"
}