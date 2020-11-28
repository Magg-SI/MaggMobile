package pl.tysia.maggstone.data.source

import pl.tysia.maggstone.data.Result
import pl.tysia.maggstone.data.model.Ware

class WareDataSourceMock {
    fun getWare(qrCode: String) : Result<Ware> {
        return Result.Success(Ware("dummy_ware_$qrCode"))
    }

    fun getWares() : Result<ArrayList<Ware>> {
        val contractors = java.util.ArrayList<Ware>()
        for (i in 1 until 10) contractors.add(Ware("dummy_ware$i"))
        return Result.Success(contractors)
    }

    fun databaseChanged() : Result<Boolean> {
        return Result.Success(false)
    }
}