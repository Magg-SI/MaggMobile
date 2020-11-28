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
import pl.tysia.maggstone.data.model.OrderedWare

class OrderedWaresViewModel(var repository: OrdersRepository) : ViewModel() {
    private val _waresResult = MutableLiveData<Int>()
    val waresResult: LiveData<Int> = _waresResult

    private val _wares = MutableLiveData<ArrayList<OrderedWare>>()
    val wares: LiveData<ArrayList<OrderedWare>> = _wares

    fun getOrder(token: String, id : Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getOrder(token, id)

            if (result is Result.Success) {
                _wares.postValue(result.data)
            } else {
                _waresResult.postValue(R.string.err_wares_download)
            }
        }
    }


}