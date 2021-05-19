package pl.tysia.maggstone.ui.wares

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.tysia.maggstone.R
import pl.tysia.maggstone.data.Result
import pl.tysia.maggstone.data.source.WareRepository
import pl.tysia.maggstone.data.model.Availability
import pl.tysia.maggstone.data.source.PictureDataSource
import java.io.IOException
import javax.inject.Inject

class WareInfoViewModel @Inject constructor(private val wareRepository: WareRepository, private val pictureDataSource: PictureDataSource) : ViewModel() {
    private val _result = MutableLiveData<Int>()
    val result: LiveData<Int> = _result

    private val _availability = MutableLiveData<ArrayList<Availability>>()
    val availability: LiveData<ArrayList<Availability>> = _availability

    private val _photo = MutableLiveData<Bitmap>()
    val photo: LiveData<Bitmap> = _photo

    fun getAvailabilities(index: String) {

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = wareRepository.getAvailabilities(index)

                if (result is Result.Success) {
                    _availability.postValue(result.data.availabilities)
                } else {
                    _result.postValue(R.string.availability_err)
                }
            }catch (e:IOException){
                _result.postValue(R.string.connection_error)

            }
        }
    }

    fun getSmallPicture(photoID: Int) {

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = pictureDataSource.getSmallPhoto(photoID)

                if (result is Result.Success) {
                    _photo.postValue(result.data)
                } else {
                    _result.postValue(R.string.availability_err)
                }
            }catch (e:IOException){
                _result.postValue(R.string.connection_error)

            }
        }
    }

}