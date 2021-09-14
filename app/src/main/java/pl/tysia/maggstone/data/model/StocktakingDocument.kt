package pl.tysia.maggstone.data.model

data class StocktakingDocument(val documentID : Int, val wares : List<DocumentItem>){
    var notFoundItems = 0
}