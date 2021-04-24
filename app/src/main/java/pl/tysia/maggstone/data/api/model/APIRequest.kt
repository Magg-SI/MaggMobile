package pl.tysia.maggstone.data.api.model

import java.io.Serializable

abstract class APIRequest(val func : String) :Serializable {
    var token : String? = null

}