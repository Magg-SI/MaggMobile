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

class OrdersViewModel(var repository: OrdersRepository) : ViewModel() {
    private val _ordersResult = MutableLiveData<Int>()
    val ordersResult: LiveData<Int> = _ordersResult

    private val _orders = MutableLiveData<ArrayList<Order>>()
    val orders: LiveData<ArrayList<Order>> = _orders

    fun getOrders(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getOrders(token)

            if (result is Result.Success) {
                _orders.postValue(result.data)
            } else {
                _ordersResult.postValue(R.string.err_orders_download)
            }
        }
    }


}