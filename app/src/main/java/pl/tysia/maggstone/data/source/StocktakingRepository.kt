package pl.tysia.maggstone.data.source

import pl.tysia.maggstone.data.Database
import pl.tysia.maggstone.data.Result
import pl.tysia.maggstone.data.model.DocumentItem
import pl.tysia.maggstone.data.model.StocktakingDocument
import javax.inject.Inject

class StocktakingRepository @Inject constructor(val db : Database, val dataSource : DocumentsDataSource) {

    fun getStocktakingDocument() : Result<StocktakingDocument>{
        val apiResponse = dataSource.getStocktakingWares()

        var notFoundNumber = 0

        if (apiResponse is Result.Success){
            val responseData = apiResponse.data
            val dao = db.waresDao()

            val documentItems = ArrayList<DocumentItem>()

            loop@ for (wareResponse in responseData.wares!!){

                val wareID = wareResponse.towID!!
                val count = wareResponse.ilosc!!

                val ware = dao.findByID(wareID)

                if (ware == null){
                    notFoundNumber++
                    continue@loop
                }

                documentItems.add(DocumentItem(ware).apply { ilosc = count })
            }

            val document = StocktakingDocument(responseData.documentID!!, documentItems)
            document.notFoundItems = notFoundNumber

            return Result.Success(document)

        }

        return apiResponse as Result.Error
    }

    fun addDocumentItem(documentID : Int, wareID: Int, count : Double) : Result<Boolean>{
        return dataSource.addStocktakingWares(documentID, wareID, count)
    }

    fun updateDocumentItem(documentID : Int, wareID : Int, count : Double) : Result<Boolean>{
        return dataSource.updateStocktakingWares(documentID, wareID, count)
    }

    fun testStocktakingPosition(documentID : Int, wareID : Int) : Result<Double>{
        return dataSource.testStocktakingPosition(documentID, wareID)
    }
}