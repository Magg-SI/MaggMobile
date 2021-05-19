package pl.tysia.maggstone.data.api.model

import java.io.Serializable

abstract class APIRequest(val func : String) : Serializable {
    var token : String? = null

    constructor(func : String, token : String) : this(func){
        this.token = token
    }

    class SmallPhotoRequest(val fotoID : Int, token : String) : APIRequest("getFotoMini", token)

    class TestWorker(val workerID : Int, token : String) : APIRequest("testWorker", token)

}