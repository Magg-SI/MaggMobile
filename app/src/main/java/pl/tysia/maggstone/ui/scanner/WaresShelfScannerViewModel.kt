package pl.tysia.maggstone.ui.scanner

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.tysia.maggstone.data.Result
import pl.tysia.maggstone.data.source.WareRepository
import pl.tysia.maggstone.data.model.Ware
import pl.tysia.maggstone.data.source.ShelfDataSource
import java.io.IOException

class WaresShelfScannerViewModel(val dataSource: ShelfDataSource) : ViewModel() {
    private val _locationResult = MutableLiveData<String>()
    val locationResult: LiveData<String> = _locationResult

     private val _locationError = MutableLiveData<String>()
    val locationError: LiveData<String> = _locationError


    fun changeLocation(wareQr : String, shelfQr : String, token : String){
        viewModelScope.launch(Dispatchers.IO) {
           try {
               val result = dataSource.addToShelf(wareQr, shelfQr, token)

               if (result is Result.Success) {
                   _locationResult.postValue(wareQr)
               } else if (result is Result.Error) {
                   _locationError.postValue(result.exception.message)
               }
           }catch (e : IOException){
               _locationError.postValue("Brak połączenia z internetem.")
           }

        }
    }

}