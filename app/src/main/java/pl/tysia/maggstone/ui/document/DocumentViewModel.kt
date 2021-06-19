package pl.tysia.maggstone.ui.document

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.tysia.maggstone.R
import pl.tysia.maggstone.data.Result
import pl.tysia.maggstone.data.api.model.APIRequest
import pl.tysia.maggstone.data.api.model.APIResponse
import pl.tysia.maggstone.data.model.DocumentItem
import pl.tysia.maggstone.data.model.Ware
import pl.tysia.maggstone.data.source.DocumentsDataSource
import java.io.IOException
import javax.inject.Inject

class DocumentViewModel @Inject constructor(val dataSource : DocumentsDataSource) : ViewModel() {
    private val _documentsResult = MutableLiveData<Int>()
    val documentsResult: LiveData<Int> = _documentsResult

    private val _progress = MutableLiveData<Int>()
    val progress: LiveData<Int> = _progress

    private val _documentsError = MutableLiveData<String>()
    val documentsError: LiveData<String> = _documentsError

    private val _getPriceResult = MutableLiveData<Ware>()
    val getPriceResult: LiveData<Ware> = _getPriceResult

    fun getWarePrice(ware : Ware, ktrID : Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = dataSource.getWarePrice(ware,ktrID)

                if (result is Result.Success) {
                    _getPriceResult.postValue(ware)
                } else {
                    _documentsError.postValue((result as Result.Error).exception.message)
                }
            }catch (e : IOException){
                _documentsError.postValue("Brak połączenia z internetem.")
            }
        }
    }

    private fun testWorker(workerID : Int) : Boolean {
        val result = dataSource.testWorker(workerID)

        return when (result.retCode) {
            APIResponse.RESPONSE_OK -> {
                _progress.postValue(100)
                Thread.sleep(100)
                _documentsResult.postValue(R.string.correct_document_send)
                true
            }
            APIResponse.WORKER_IN_PROGRESS -> {
                _progress.postValue(result.procent)
                false
            }
            else -> {
                _documentsError.postValue(result.retMessage)
                true
            }
        }
    }

    private fun watchProgress(workerID : Int){
        var sendingFinished = false
        while (!sendingFinished){
            sendingFinished = testWorker(workerID)
            Thread.sleep(500)
        }
    }

    fun sendOfferDocument( id : Int, sign : String, comments : String, items : List<DocumentItem>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = dataSource.sendDocument( id, sign, comments, items)

                if (result is Result.Success) {
                    watchProgress(result.data)
                } else {
                    _documentsError.postValue((result as Result.Error).exception.message)
                }
            }catch (e : IOException){
                _documentsError.postValue("Brak połączenia z internetem.")
            }
        }
    }

    fun sendShiftDocument(id: Int, sign: String, comments: String, items: List<DocumentItem>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = dataSource.sendShiftDocument(id, sign, comments, items)

                if (result is Result.Success) {
                    watchProgress(result.data)
                } else {
                    _documentsError.postValue((result as Result.Error).exception.message)
                }
            }catch (e : IOException){
                _documentsError.postValue("Brak połączenia z internetem.")

            }

        }
    }
}