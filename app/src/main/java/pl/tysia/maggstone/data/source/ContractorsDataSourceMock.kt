package pl.tysia.maggstone.data.source

import pl.tysia.maggstone.data.Result
import pl.tysia.maggstone.data.model.Contractor
import java.util.ArrayList

@Deprecated("Stop mocking me!")
class ContractorsDataSourceMock {
    fun getContractors(token : String, counter : Int) : Result<ArrayList<Contractor>> {
        val contractors = ArrayList<Contractor>()
        for (i in 1 until 10) contractors.add(Contractor(i, "Index$i", "Name$i"))
        return Result.Success(contractors)
    }
}