package pl.tysia.maggstone.ui.service

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.tysia.maggstone.R
import pl.tysia.maggstone.data.Result
import pl.tysia.maggstone.data.model.Service
import pl.tysia.maggstone.data.source.*
import java.io.IOException

class ServiceViewModel(var datasource: ServiceDataSource) : ViewModel() {
    private val _serviceResult = MutableLiveData<Int>()
    val serviceResult: LiveData<Int> = _serviceResult

    private val _service = MutableLiveData<Service>()
    val service: LiveData<Service> = _service

    fun addService(service: Service) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = datasource.addService(service)

                if (result is Result.Success) {
                    _service.postValue(result.data)
                } else {
                    _serviceResult.postValue(R.string.error_couldnt_create_service)
                }
            }catch (ex : IOException){
                _serviceResult.postValue(R.string.connection_error)
            }

        }
    }


}