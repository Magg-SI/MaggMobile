package pl.tysia.maggstone.ui.cooperation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.tysia.maggstone.R
import pl.tysia.maggstone.data.Result
import pl.tysia.maggstone.data.model.Document
import pl.tysia.maggstone.data.model.Warehouse
import pl.tysia.maggstone.data.source.ContractorsDataSource
import java.io.IOException
import javax.inject.Inject

class ContractorsViewModel @Inject constructor(var datasource: ContractorsDataSource) : ViewModel() {
    private val _contractorsResult = MutableLiveData<Int>()
    val contractorsResult: LiveData<Int> = _contractorsResult

    private val _cooperationHistory = MutableLiveData<ArrayList<Document>>()
    val cooperationHistory: LiveData<ArrayList<Document>> = _cooperationHistory

    fun getCooperationHistory(id : Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = datasource.getCooperationHistory(id)

                if (result is Result.Success) {
                    _cooperationHistory.postValue(result.data)
                } else {
                    _contractorsResult.postValue(R.string.error_couldnt_download_history)
                }
            }catch (ex : IOException){
                _contractorsResult.postValue(R.string.connection_error)
            }

        }
    }


}