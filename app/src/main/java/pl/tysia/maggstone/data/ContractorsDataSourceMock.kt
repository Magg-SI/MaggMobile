package pl.tysia.maggstone.data

import pl.tysia.maggstone.data.model.Contractor
import java.util.ArrayList

class ContractorsDataSourceMock {
    fun getContractors() : Result<ArrayList<Contractor>> {
        val contractors = ArrayList<Contractor>()
        for (i in 1 until 10) contractors.add(Contractor(i, "Index$i", "Name$i"))
        return Result.Success(contractors)
    }
}