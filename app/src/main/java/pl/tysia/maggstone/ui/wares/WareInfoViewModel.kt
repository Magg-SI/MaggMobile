package pl.tysia.maggstone.ui.wares

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pl.tysia.maggstone.data.Result
import pl.tysia.maggstone.data.source.WareRepository
import pl.tysia.maggstone.data.model.Availability
import pl.tysia.maggstone.data.service.notifyObserver

class WareInfoViewModel(private val wareRepository: WareRepository) : ViewModel() {
    private val _result = MutableLiveData<Int>()
    val result: LiveData<Int> = _result

    private val _availability = MutableLiveData<ArrayList<Availability>>()
    val availability: LiveData<ArrayList<Availability>> = _availability


    fun getAvailabilities(index: String, token: String) {
        val result = wareRepository.getAvailabilities(index, token)

        if (result is Result.Success) {
            _availability.value = result.data.availabilities
            _availability.notifyObserver()
        } else {
            //TODO:
            // _wareResult.value = R.string.err_contractors_download
        }
    }
}