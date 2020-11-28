package pl.tysia.maggstone.data.source

import pl.tysia.maggstone.data.Result
import pl.tysia.maggstone.data.model.Contractor

@Deprecated("Redundant")
class ContractorsRepository(val dataSource: ContractorsDataSourceMock) {
    fun getContractors() : Result<ArrayList<Contractor>> {
//        return dataSource.getContractors()
        TODO()
    }
}