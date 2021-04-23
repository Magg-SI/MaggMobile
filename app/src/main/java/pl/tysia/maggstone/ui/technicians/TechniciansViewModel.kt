package pl.tysia.maggstone.ui.technicians

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
import pl.tysia.maggstone.data.model.Technician
import pl.tysia.maggstone.data.model.Warehouse
import pl.tysia.maggstone.data.source.TechniciansDataSource
import pl.tysia.maggstone.data.source.WareDataSource
import pl.tysia.maggstone.data.source.WarehousesDataSource
import java.io.IOException

class TechniciansViewModel(var datasource: TechniciansDataSource) : ViewModel() {
    private val _techniciansResult = MutableLiveData<Int>()
    val techniciansResult: LiveData<Int> = _techniciansResult

    private val _technicians = MutableLiveData<ArrayList<Technician>>()
    val orders: LiveData<ArrayList<Technician>> = _technicians

    fun getTechnicians(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = datasource.getTechnicians(token)

                if (result is Result.Success) {
                    _technicians.postValue(result.data)
                } else {
                    _techniciansResult.postValue(R.string.error_coudnt_download_technicians)
                }
            }catch (ex : IOException){
                _techniciansResult.postValue(R.string.connection_error)
            }

        }
    }


}