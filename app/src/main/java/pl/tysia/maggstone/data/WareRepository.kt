package pl.tysia.maggstone.data

import pl.tysia.maggstone.data.model.Ware

class WareRepository(val dataSource : WareDataSourceMock) {
    fun getWare(qrCode: String) : Result<Ware>{
        return dataSource.getWare(qrCode)
    }

    fun getWares() : Result<ArrayList<Ware>>{
        return dataSource.getWares()
    }
}