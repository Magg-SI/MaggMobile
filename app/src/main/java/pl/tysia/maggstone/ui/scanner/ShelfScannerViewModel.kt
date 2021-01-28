package pl.tysia.maggstone.ui.scanner

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.tysia.maggstone.R
import pl.tysia.maggstone.data.Result
import pl.tysia.maggstone.data.api.service.ShelfService
import pl.tysia.maggstone.data.source.ShelfDataSource
import java.io.IOException

class ShelfScannerViewModel(private val dataSource : ShelfDataSource) : ViewModel(){
    private val _result = MutableLiveData<Boolean>()
    val result: LiveData<Boolean> = _result

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun testShelf(shelfCode: String, token : String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = dataSource.testShelf(shelfCode, token)

                if (result is Result.Success) {
                    _result.postValue(true)
                } else if (result is Result.Error) {
                    _error.postValue(result.exception.message)
                }
            }catch (e : IOException){
                _error.postValue("Brak połączenia z internetem.")
            }

        }


    }
}