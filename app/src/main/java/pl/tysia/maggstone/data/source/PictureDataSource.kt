package pl.tysia.maggstone.data.source

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.preference.PreferenceManager
import pl.tysia.maggstone.data.NetAddressManager
import pl.tysia.maggstone.data.Result
import pl.tysia.maggstone.data.api.model.APIResponse
import pl.tysia.maggstone.data.api.model.GetPictureRequest
import pl.tysia.maggstone.data.api.model.UpdatePictureRequest
import pl.tysia.maggstone.data.api.service.WareService
import pl.tysia.maggstone.data.model.Ware
import pl.tysia.maggstone.resizeBitmap
import pl.tysia.maggstone.ui.picture.PictureEditorActivity
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.lang.Exception


class PictureDataSource(val context: Context, netAddressManager: NetAddressManager) : APISource(netAddressManager) {
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private lateinit var service : WareService

    init {
        service = retrofit.create(WareService::class.java)
    }

    companion object{
        private fun getPictureSize(context: Context) : Float{

            return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString("picture_size", "0.1")!!.toFloat()
        }
    }

    fun sendPictureStart(wareID : Int, photoBatch : String, token : String) : Result<Int> {
        val result = service.updatePictureStart(
            UpdatePictureRequest.Start(token = token, towID = wareID, photo = photoBatch)
        ).execute()

        return if (result.body()!!.retCode == APIResponse.RESPONSE_OK){
            Result.Success(result.body()!!.fotoID!!)
        }else{
            Result.Error(Exception(result.body()!!.retMessage))
        }
    }

    fun sendPictureNext(photoID : Int, photoBatch : String, pageNr : Int, token : String) : Result<Boolean> {
        val result = service.updatePictureNext(
            UpdatePictureRequest.Next(token = token, fotoID = photoID, photo = photoBatch, pageNo = pageNr)
        ).execute()

        return if (result.body()!!.retCode == APIResponse.RESPONSE_OK){
            Result.Success(result.body()!!.retCode == APIResponse.RESPONSE_OK)
        }else{
            Result.Error(Exception(result.body()!!.retMessage))
        }
    }

    fun sendPictureFin(token : String, photoID: Int) : Result<Boolean> {
        val result = service.updatePictureFin(
            UpdatePictureRequest.Finalize(token = token, fotoID = photoID)
        ).execute()

        return if (result.body()!!.retCode == APIResponse.RESPONSE_OK){
            Result.Success(result.body()!!.retCode == APIResponse.RESPONSE_OK)
        }else{
            Result.Error(Exception(result.body()!!.retMessage))
        }
    }

    fun getPicture(wareID: Int, token : String) : Result<Bitmap> {
        val result = service.getPicture(
            GetPictureRequest(token = token, towID = wareID)
        ).execute()

        return if (result.body()!!.retCode == APIResponse.RESPONSE_OK){
            Result.Success(photoFromString(result.body()!!.picture!!))
        }else{
            Result.Error(Exception(result.body()!!.retMessage))
        }
    }

    private fun photoFromString(string : String) :  Bitmap{
        val imageBytes = Base64.decode(string, 0)

        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }


}