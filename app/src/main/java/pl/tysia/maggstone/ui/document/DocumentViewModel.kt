package pl.tysia.maggstone.ui.document

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.tysia.maggstone.R
import pl.tysia.maggstone.data.Result
import pl.tysia.maggstone.data.model.DocumentItem
import pl.tysia.maggstone.data.source.DocumentsDataSource
import java.io.IOException
import javax.inject.Inject

class DocumentViewModel @Inject constructor(val dataSource : DocumentsDataSource) : ViewModel() {
    private val _documentsResult = MutableLiveData<Int>()
    val documentsResult: LiveData<Int> = _documentsResult

    private val _documentsError = MutableLiveData<String>()
    val documentsError: LiveData<String> = _documentsError

    fun sendOfferDocument( id : Int, sign : String, comments : String, items : List<DocumentItem>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = dataSource.sendDocument( id, sign, comments, items)

                if (result is Result.Success) {
                    _documentsResult.postValue(R.string.correct_document_send)
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
                    _documentsResult.postValue(R.string.correct_document_send)
                } else {
                    _documentsError.postValue((result as Result.Error).exception.message)
                }
            }catch (e : IOException){
                _documentsError.postValue("Brak połączenia z internetem.")

            }

        }
    }
}