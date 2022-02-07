package pl.tysia.maggstone.ui.service

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.tysia.maggstone.R
import pl.tysia.maggstone.data.Result
import pl.tysia.maggstone.data.model.ServicePrice
import pl.tysia.maggstone.data.source.PriceListRepository
import java.io.IOException
import javax.inject.Inject

class ServicePriceListViewModel @Inject constructor(var repository: PriceListRepository) : ViewModel() {
    private val _priceListResult = MutableLiveData<Int>()
    val priceListResult: LiveData<Int> = _priceListResult

    private val _priceList = MutableLiveData<ArrayList<ServicePrice>>()
    val priceList: LiveData<ArrayList<ServicePrice>> = _priceList

    fun getOrders() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = repository.getPriceList()

                if (result is Result.Success) {
                    _priceList.postValue(result.data)
                } else {
                    _priceListResult.postValue(R.string.err_orders_download)
                }
            }catch (ex : IOException){
                _priceListResult.postValue(R.string.connection_error)
            }

        }
    }


}