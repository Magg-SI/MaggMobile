package pl.tysia.maggstone.data

import pl.tysia.maggstone.data.model.Contractor
import pl.tysia.maggstone.data.model.Ware

class WareDataSourceMock {
    fun getWare(qrCode: String) : Result<Ware> {
        return Result.Success(Ware("test_ware_$qrCode"))
    }

    fun getWares() : Result<ArrayList<Ware>> {
        val contractors = java.util.ArrayList<Ware>()
        for (i in 1 until 10) contractors.add(Ware("Ware$i"))
        return Result.Success(contractors)
    }

    fun databaseChanged() : Result<Boolean> {
        return Result.Success(false)
    }
}