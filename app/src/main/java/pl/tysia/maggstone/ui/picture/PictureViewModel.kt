package pl.tysia.maggstone.ui.picture

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.tysia.maggstone.R
import pl.tysia.maggstone.data.source.PictureDataSource
import pl.tysia.maggstone.data.Result
import java.io.IOException
import java.lang.Exception
import javax.inject.Inject

class PictureViewModel @Inject constructor(var pictureDataSource: PictureDataSource) : ViewModel() {
    private val _pictureResult = MutableLiveData<Int>()
    val pictureResult: LiveData<Int> = _pictureResult

    private val _picture = MutableLiveData<Bitmap>()
    val picture: LiveData<Bitmap> = _picture

    fun getPicture(wareID: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = pictureDataSource.getPicture(wareID)

                if (result is Result.Success) {
                    _picture.postValue(result.data)
                } else {
                    _pictureResult.postValue(R.string.no_picture)
                }
            }catch (ex : IOException){
                _pictureResult.postValue(R.string.connection_error)
            }

        }
    }

}