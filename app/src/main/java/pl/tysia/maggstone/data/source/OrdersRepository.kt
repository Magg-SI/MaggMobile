package pl.tysia.maggstone.data.source

import pl.tysia.maggstone.data.Result
import pl.tysia.maggstone.data.model.Order
import pl.tysia.maggstone.data.model.OrderedWare
import javax.inject.Inject

class OrdersRepository @Inject constructor(private var dataSource : OrdersDataSource){

    fun getOrders() : Result<ArrayList<Order>> = dataSource.getOrders()


    fun getOrder(id : Int): Result<ArrayList<OrderedWare>> = dataSource.getOrder( id)

    fun packWare(id : Int,
                 packedNumber : Double,
                 postponedNumber : Double,
                 cancelledNumber : Double): Result<Boolean> =
        dataSource.packWare(id,packedNumber,postponedNumber,cancelledNumber)

}