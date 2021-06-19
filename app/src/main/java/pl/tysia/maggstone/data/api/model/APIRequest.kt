package pl.tysia.maggstone.data.api.model

import java.io.Serializable

abstract class APIRequest(val func : String) : Serializable {
    var token : String? = null

    constructor(func : String, token : String) : this(func){
        this.token = token
    }

    //class SmallPhotoRequest(val fotoID : Int, token : String) : APIRequest("getFotoMini", token)
    class SmallPhotoRequest(val towID : Int, token : String) : APIRequest("getFotoMini2", token)

    class GetWarePrice(val towID : Int, val ktrID : Int, token : String) : APIRequest("dajCene", token)

    class TestWorker(val workerID : Int, token : String) : APIRequest("testWorker", token)

    class OrderWare(val towID : Int, val ilosc : Double, val uwagi : String, token : String) : APIRequest("zamowOnePoz", token)

}