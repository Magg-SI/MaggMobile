package pl.tysia.maggstone.ui.stocktaking

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.tysia.maggstone.R
import pl.tysia.maggstone.data.Result
import pl.tysia.maggstone.data.model.DocumentItem
import pl.tysia.maggstone.data.model.StocktakingDocument
import pl.tysia.maggstone.data.source.StocktakingRepository
import java.io.IOException
import javax.inject.Inject

class StocktakingViewModel @Inject constructor(var repository: StocktakingRepository) : ViewModel() {
    private val _result = MutableLiveData<Int>()
    val result: LiveData<Int> = _result

    private val _stocktakingDocument = MutableLiveData<StocktakingDocument>()
    val stocktakingDocument: LiveData<StocktakingDocument> = _stocktakingDocument

    private val _wareResult = MutableLiveData<Boolean>()
    val wareResult: LiveData<Boolean> = _wareResult

    private val _testResult = MutableLiveData<Double>()
    val testResult: LiveData<Double> = _testResult

    fun testStocktakingPosition(documentID : Int, wareID : Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = repository.testStocktakingPosition(documentID, wareID)

                if (result is Result.Success) {
                    _testResult.postValue(result.data)
                } else {
                    _result.postValue(R.string.connection_error_generic)
                }
            }catch (ex : IOException){
                _result.postValue(R.string.connection_error)
            }
        }
    }

    fun getStocktakingDocument() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = repository.getStocktakingDocument()

                if (result is Result.Success) {
                    _stocktakingDocument.postValue(result.data)
                } else {
                    _result.postValue(R.string.err_couldnt_download_document)
                }
            }catch (ex : IOException){
                _result.postValue(R.string.connection_error)
            }

        }
    }

    fun addDocumentItem(documentID : Int, wareID : Int, count : Double) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = repository.addDocumentItem(documentID, wareID, count)

                if (result is Result.Success) {
                    _wareResult.postValue(result.data)
                } else {
                    _result.postValue(R.string.err_add_ware_stocktaking)
                }
            }catch (ex : IOException){
                _result.postValue(R.string.connection_error)
            }

        }
    }

    fun updateDocumentItem(documentID : Int, wareID : Int, count : Double) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = repository.updateDocumentItem(documentID, wareID, count)

                if (result is Result.Success) {
                    _wareResult.postValue(result.data)
                } else {
                    _result.postValue(R.string.err_add_ware_stocktaking)
                }
            }catch (ex : IOException){
                _result.postValue(R.string.connection_error)
            }

        }
    }

}