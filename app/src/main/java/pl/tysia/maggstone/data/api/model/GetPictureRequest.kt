package pl.tysia.maggstone.data.api.model

class GetPictureRequest (var token : String, var fotoID : Int, val func : String = "getFotoByID" ) {
}