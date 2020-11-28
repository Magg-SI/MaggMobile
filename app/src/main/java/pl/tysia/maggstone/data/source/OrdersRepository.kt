package pl.tysia.maggstone.data.source

import pl.tysia.maggstone.data.Result
import pl.tysia.maggstone.data.model.Order
import pl.tysia.maggstone.data.model.OrderedWare

class OrdersRepository(private var dataSource : OrdersDataSource){

    fun getOrders(token : String) : Result<ArrayList<Order>> = dataSource.getOrders(token)


    fun getOrder(token : String, id : Int): Result<ArrayList<OrderedWare>> = dataSource.getOrder(token, id)

    fun packWare(token : String,
                 id : Int,
                 packedNumber : Double,
                 postponedNumber : Double,
                 cancelledNumber : Double): Result<Boolean> =
        dataSource.packWare(token,id,packedNumber,postponedNumber,cancelledNumber)

}