package pl.tysia.maggstone.ui.wares

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.tysia.maggstone.R
import pl.tysia.maggstone.data.Result
import pl.tysia.maggstone.data.dao.WaresDAO
import pl.tysia.maggstone.data.source.WareRepository
import pl.tysia.maggstone.data.model.Ware
import java.io.IOException

class WareViewModel(val wareRepository: WareRepository, val dao : WaresDAO) : ViewModel() {
    private val _wareResult = MutableLiveData<String>()
    val wareResult: LiveData<String> = _wareResult

    private val _ware = MutableLiveData<Ware>()
    val ware: LiveData<Ware> = _ware

    fun getWare(qrCode: String, token : String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = wareRepository.getWare(qrCode, token)

                if (result is Result.Success) {
                    _ware.postValue(result.data)
                } else if (result is Result.Error){
                    _wareResult.postValue(result.exception.message!!)
                }

            }catch (ex : IOException){
                getFromLocalBase(qrCode)
            }

        }

    }

    private fun getFromLocalBase(qrCode: String){
        viewModelScope.launch(Dispatchers.IO) {
            val result = dao.findByQr(qrCode)

            if (result == null) _wareResult.postValue("Nie znaleziono towaru.")
            else _ware.postValue(result)

        }
    }


}