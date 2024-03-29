package pl.tysia.maggstone.ui.orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.tysia.maggstone.R
import pl.tysia.maggstone.data.source.OrdersRepository
import pl.tysia.maggstone.data.Result
import pl.tysia.maggstone.data.model.Order
import java.io.IOException
import javax.inject.Inject

class OrdersViewModel @Inject constructor(var repository: OrdersRepository) : ViewModel() {
    private val _ordersResult = MutableLiveData<Int>()
    val ordersResult: LiveData<Int> = _ordersResult

    private val _orders = MutableLiveData<ArrayList<Order>>()
    val orders: LiveData<ArrayList<Order>> = _orders

    fun getOrders() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = repository.getOrders()

                if (result is Result.Success) {
                    _orders.postValue(result.data)
                } else {
                    _ordersResult.postValue(R.string.err_orders_download)
                }
            }catch (ex : IOException){
                _ordersResult.postValue(R.string.connection_error)
            }

        }
    }


}