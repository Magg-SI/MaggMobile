package pl.tysia.maggstone.data.api.model

class GetPictureRequest (var token : String, var towID : Int, val func : String = "findTowar" ) {
}