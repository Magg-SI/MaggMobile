package pl.tysia.maggstone.ui.hose

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.tysia.maggstone.R
import pl.tysia.maggstone.data.Database
import pl.tysia.maggstone.data.Result
import pl.tysia.maggstone.data.dao.WaresDAO
import pl.tysia.maggstone.data.model.Hose
import pl.tysia.maggstone.data.model.Ware
import pl.tysia.maggstone.data.source.WareDataSource
import java.io.IOException
import javax.inject.Inject

class HoseViewModel @Inject constructor(val db : Database, val remoteDataSource : WareDataSource) : ViewModel() {
    private val _hoseForm = MutableLiveData<HoseFormState>()
    val hoseForm: LiveData<HoseFormState> = _hoseForm

    private val _result = MutableLiveData<String>()
    val result : LiveData<String> = _result

    private val _hoseResult = MutableLiveData<Hose>()
    val hoseResult : LiveData<Hose> = _hoseResult

    private val hoseFormState =  HoseFormState()
    val hose =  Hose()

    private val localDataSource : WaresDAO = db.waresDao()

    fun formChanged(length : String, code: String, angle : String, creator : String){
        if (!length.isBlank()) {
            hose.length = length.toInt()
        }

        hoseFormState.lengthValid = length.isNotBlank()

        hose.code = code
        hose.angle = angle
        hose.creator = creator

        _hoseForm.postValue(hoseFormState)

    }

    fun onCordChanged(cord : Ware?){
        hose.cord = cord
        hose.sleeve = null
        hose.tip1 = null
        hose.tip2 = null

        hoseFormState.cordError = null

        if (cord != null) hoseFormState.cordValid = true

        _hoseForm.postValue(hoseFormState)
    }

    fun onCordChanged(cordId : String){
        viewModelScope.launch(Dispatchers.IO) {
            val ware = getWareByID(cordId)
            if (ware == null || !isCord(ware)) {
                hoseFormState.cordError = R.string.cord_not_found
                hose.cord = null
                _hoseForm.postValue(hoseFormState)
            } else onCordChanged(ware)
        }
    }

    fun onSleeveChanged(sleeve : Ware?){
        hose.sleeve = sleeve
        hoseFormState.sleeveValid = true
        hoseFormState.sleeveError = null
        _hoseForm.postValue(hoseFormState)
    }

    fun onSleeveChanged(sleeveId : String){
        viewModelScope.launch(Dispatchers.IO) {
            val ware = getWareByID(sleeveId)
            if (ware == null || !isSleeve(ware)) {
                hoseFormState.sleeveError = R.string.sleeve_not_found
                hose.sleeve = null
                _hoseForm.postValue(hoseFormState)
            } else onSleeveChanged(ware)
        }
    }

    fun onTip1Changed(tip : Ware?){
        hose.tip1 = tip
        hoseFormState.tip1Valid = true
        hoseFormState.tip1Error = null
        _hoseForm.postValue(hoseFormState)
    }

    fun onTip1Changed(tipId : String){
        viewModelScope.launch(Dispatchers.IO) {
            val ware = getWareByID(tipId)
            if (ware == null || !isTip(ware)) {
                hoseFormState.tip1Error = R.string.tip_not_found
                hose.tip1 = null
                _hoseForm.postValue(hoseFormState)
            } else onTip1Changed(ware)
        }
    }

    fun onTip2Changed(tip : Ware?){
        hose.tip2 = tip
        hoseFormState.tip2Valid = true
        hoseFormState.tip2Error = null
        _hoseForm.postValue(hoseFormState)
    }

    fun onTip2Changed(tipId : String){
        viewModelScope.launch(Dispatchers.IO) {
            val ware = getWareByID(tipId)
            if (ware == null || !isTip(ware)) {
                hoseFormState.tip2Error = R.string.tip_not_found
                hose.tip2 = null
                _hoseForm.postValue(hoseFormState)
            } else onTip2Changed(ware)
        }
    }

    fun getHose( hoseNumber : String){
        viewModelScope.launch(Dispatchers.IO) {
            try{
                val result = remoteDataSource.findHose(hoseNumber)

                if (result is Result.Success) {
                    _hoseResult.postValue(result.data)
                } else if (result is Result.Error) {
                    _result.postValue(result.exception.message!!)
                }
            }catch (e : IOException){
                _result.postValue("Brak połączenia z internetem.")
            }

        }
    }

    fun addHose( hose : Hose){
        viewModelScope.launch(Dispatchers.IO) {
            try{
                val result = remoteDataSource.addHose(hose)

                if (result is Result.Success) {
                    _hoseResult.postValue(result.data)
                } else if (result is Result.Error) {
                    _result.postValue(result.exception.message!!)
                }
            }catch (e : IOException){
                _result.postValue("Brak połączenia z internetem.")
            }

        }
    }

    private fun isTip(ware :Ware): Boolean = ware.hoseType == Ware.HOSE_TYPE_TIP
    private fun isCord(ware :Ware): Boolean = ware.hoseType == Ware.HOSE_TYPE_CORD
    private fun isSleeve(ware :Ware): Boolean = ware.hoseType == Ware.HOSE_TYPE_SLEEVE


    private fun getWareByID(wareId : String) : Ware? {
        return localDataSource.findByIndex(wareId)
    }

}