package pl.tysia.maggstone.data.source

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import pl.tysia.maggstone.data.Result
import pl.tysia.maggstone.data.api.model.APIRequest
import pl.tysia.maggstone.data.api.model.APIResponse
import pl.tysia.maggstone.data.api.model.GetPictureRequest
import pl.tysia.maggstone.data.api.model.UpdatePictureRequest
import pl.tysia.maggstone.data.api.service.WareService
import retrofit2.Retrofit
import java.lang.Exception
import javax.inject.Inject


class PictureDataSource @Inject constructor(val context: Context, val retrofit: Retrofit, val tokenProvider: TokenProvider) {
    private var service : WareService = retrofit.create(WareService::class.java)

    fun getSmallPhoto(photoID : Int) : Result<Bitmap> {
        val service = retrofit.create(WareService::class.java)

        val result = service.getSmallPicture(
            APIRequest.SmallPhotoRequest(photoID, tokenProvider.getToken()!!)
        ).execute()

        return if (result.body()!!.retCode == APIResponse.RESPONSE_OK){
            Result.Success(photoFromString(result.body()!!.picture!!))
        }else{
            Result.Error(Exception(result.body()!!.retMessage))
        }
    }

    fun sendPictureStart(wareID : Int, photoBatch : String) : Result<Int> {
        val result = service.updatePictureStart(
            UpdatePictureRequest.Start(token = tokenProvider.getToken()!!, towID = wareID, photo = photoBatch)
        ).execute()

        return if (result.body()!!.retCode == APIResponse.RESPONSE_OK){
            Result.Success(result.body()!!.fotoID!!)
        }else{
            Result.Error(Exception(result.body()!!.retMessage))
        }
    }

    fun sendPictureNext(photoID : Int, photoBatch : String, pageNr : Int) : Result<Boolean> {
        val result = service.updatePictureNext(
            UpdatePictureRequest.Next(token = tokenProvider.getToken()!!, fotoID = photoID, photo = photoBatch, pageNo = pageNr)
        ).execute()

        return if (result.body()!!.retCode == APIResponse.RESPONSE_OK){
            Result.Success(result.body()!!.retCode == APIResponse.RESPONSE_OK)
        }else{
            Result.Error(Exception(result.body()!!.retMessage))
        }
    }

    fun sendPictureFin(photoID: Int) : Result<Boolean> {
        val result = service.updatePictureFin(
            UpdatePictureRequest.Finalize(token = tokenProvider.getToken()!!, fotoID = photoID)
        ).execute()

        return if (result.body()!!.retCode == APIResponse.RESPONSE_OK){
            Result.Success(result.body()!!.retCode == APIResponse.RESPONSE_OK)
        }else{
            Result.Error(Exception(result.body()!!.retMessage))
        }
    }

    fun getPicture(photoID: Int) : Result<Bitmap> {
        val result = service.getPicture(
            GetPictureRequest(token = tokenProvider.getToken()!!, fotoID = photoID)
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