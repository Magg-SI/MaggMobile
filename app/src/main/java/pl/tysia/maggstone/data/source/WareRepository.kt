package pl.tysia.maggstone.data.source

import pl.tysia.maggstone.data.Result
import pl.tysia.maggstone.data.api.model.APIResponse
import pl.tysia.maggstone.data.model.Ware

@Deprecated("Redundant")
class WareRepository(val dataSource : WareDataSource) {
    fun getWare(qrCode: String, token : String) : Result<Ware> {
        return dataSource.getWare(qrCode, token)
    }

    fun getWares() : Result<ArrayList<Ware>> {
        TODO()
    }

    fun getAvailabilities(index: String, token : String) : Result<APIResponse.Availabilities> {
        return dataSource.getAvailabilities(index, token)
    }
}