package pl.tysia.maggstone.data.source

import pl.tysia.maggstone.data.Result
import pl.tysia.maggstone.data.api.model.APIResponse
import pl.tysia.maggstone.data.model.Ware
import javax.inject.Inject

class WareRepository @Inject constructor(val dataSource : WareDataSource) {
    fun getWare(qrCode: String) : Result<Ware> {
        return dataSource.getWare(qrCode)
    }

    fun orderWare(id : Int, number : Double, comments : String) : Result<Boolean> {
        return dataSource.orderWare(id, number, comments)
    }


    fun getAvailabilities(index: String) : Result<APIResponse.Availabilities> {
        return dataSource.getAvailabilities(index)
    }

}