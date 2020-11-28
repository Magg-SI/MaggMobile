package pl.tysia.maggstone.ui.picture

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pl.tysia.maggstone.data.source.PictureDataSource
import pl.tysia.maggstone.data.Result

class PictureViewModel(var pictureDataSource: PictureDataSource) : ViewModel() {
    private val _pictureResult = MutableLiveData<Int>()
    val pictureResult: LiveData<Int> = _pictureResult

    private val _picture = MutableLiveData<Bitmap>()
    val picture: LiveData<Bitmap> = _picture


    fun getPicture(wareID: Int, token : String) {
        val result = pictureDataSource.getPicture(wareID, token)

        if (result is Result.Success) {
            _picture.value = result.data
        } else {
            //TODO:
            // _wareResult.value = R.string.err_contractors_download
        }
    }


}