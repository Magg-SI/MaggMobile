package pl.tysia.maggstone.ui.warehouses

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
import pl.tysia.maggstone.data.model.Warehouse
import pl.tysia.maggstone.data.source.WareDataSource
import pl.tysia.maggstone.data.source.WarehousesDataSource
import java.io.IOException

class WarehousesViewModel(var datasource: WarehousesDataSource) : ViewModel() {
    private val _warehousesResult = MutableLiveData<Int>()
    val warehousesResult: LiveData<Int> = _warehousesResult

    private val _warehouses = MutableLiveData<ArrayList<Warehouse>>()
    val orders: LiveData<ArrayList<Warehouse>> = _warehouses

    fun getWarehouses(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = datasource.getWarehouses(token)

                if (result is Result.Success) {
                    _warehouses.postValue(result.data)
                } else {
                    _warehousesResult.postValue(R.string.error_cant_download_ontractors)
                }
            }catch (ex : IOException){
                _warehousesResult.postValue(R.string.connection_error)
            }

        }
    }


}