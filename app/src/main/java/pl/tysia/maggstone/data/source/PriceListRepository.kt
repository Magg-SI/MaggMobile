package pl.tysia.maggstone.data.source

import pl.tysia.maggstone.data.Result
import pl.tysia.maggstone.data.model.ServicePrice
import javax.inject.Inject

class PriceListRepository @Inject constructor(private var dataSource : ServicePriceListDataSource){

    fun getPriceList() : Result<ArrayList<ServicePrice>> = dataSource.getPriceList()

}