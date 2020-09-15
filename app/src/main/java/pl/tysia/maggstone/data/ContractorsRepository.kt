package pl.tysia.maggstone.data

import pl.tysia.maggstone.data.ContractorsDataSourceMock
import pl.tysia.maggstone.data.Result
import pl.tysia.maggstone.data.model.Contractor

class ContractorsRepository(val dataSource: ContractorsDataSourceMock) {
    fun getContractors() : Result<ArrayList<Contractor>> {
        return dataSource.getContractors()
    }
}