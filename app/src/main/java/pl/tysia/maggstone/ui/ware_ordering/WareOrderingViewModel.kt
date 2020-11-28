package pl.tysia.maggstone.ui.ware_ordering

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.tysia.maggstone.R
import pl.tysia.maggstone.data.source.OrdersRepository
import pl.tysia.maggstone.data.Result

class WareOrderingViewModel(var repository: OrdersRepository) : ViewModel() {
    private val _packResult = MutableLiveData<Boolean>()
    val packResult: LiveData<Boolean> = _packResult

    private val _waresResult = MutableLiveData<Int>()
    val waresResult: LiveData<Int> = _waresResult

    fun packWare(token : String,
                 id : Int,
                 packedNumber : Double,
                 postponedNumber : Double,
                 cancelledNumber : Double) {

        viewModelScope.launch(Dispatchers.IO) {
            val result =
                repository.packWare(token, id, packedNumber, postponedNumber, cancelledNumber)

            if (result is Result.Success) {
                _packResult.postValue(result.data)
            } else {
                _waresResult.postValue(R.string.err_wares_download)
            }
        }
    }



}