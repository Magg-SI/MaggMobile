package pl.tysia.maggstone.ui.wares

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.zxing.qrcode.encoder.QRCode
import pl.tysia.maggstone.R
import pl.tysia.maggstone.data.Result
import pl.tysia.maggstone.data.WareRepository
import pl.tysia.maggstone.data.model.Contractor
import pl.tysia.maggstone.data.model.Ware

class WareViewModel(val wareRepository: WareRepository) : ViewModel() {
    private val _wareResult = MutableLiveData<Int>()
    val wareResult: LiveData<Int> = _wareResult

    private val _ware = MutableLiveData<Ware>()
    val ware: LiveData<Ware> = _ware


    private val _wares = MutableLiveData<ArrayList<Ware>>()
    val wares: LiveData<ArrayList<Ware>> = _wares

    fun getWare(qrCode: String) {
        val result = wareRepository.getWare(qrCode)

        if (result is Result.Success) {
            _ware.value = result.data
        } else {
            //TODO:
            // _wareResult.value = R.string.err_contractors_download
        }
    }

    fun getWares() {
        val result = wareRepository.getWares()

        if (result is Result.Success) {
            _wares.value = result.data
        } else {
            //TODO:
            // _wareResult.value = R.string.err_contractors_download
        }
    }

}