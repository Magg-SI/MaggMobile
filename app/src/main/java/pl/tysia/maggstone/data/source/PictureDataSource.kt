package pl.tysia.maggstone.data.source

import android.content.Context
import android.graphics.Bitmap
import android.preference.PreferenceManager
import pl.tysia.maggstone.data.Result
import pl.tysia.maggstone.data.api.model.APIResponse
import pl.tysia.maggstone.data.api.model.GetPictureRequest
import pl.tysia.maggstone.data.api.model.UpdatePictureRequest
import pl.tysia.maggstone.data.api.service.WareService
import pl.tysia.maggstone.data.model.Ware
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

private const val BASE_URL = "http://martech.magg.pl/"

class PictureDataSource(val context: Context) {
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    companion object{
        private fun getPictureSize(context: Context) : Float{

            return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString("picture_size", "0.1")!!.toFloat()
        }
    }

    fun sendPicture(ware: Ware, token : String) : Result<Boolean> {
        val service = retrofit.create(WareService::class.java)


        val result = service.updatePicture(
            UpdatePictureRequest(token = token, towID = ware.id!!, photo = ware.photoString!!)
        ).execute()

        return if (result.body()!!.retCode == APIResponse.RESPONSE_OK){
            Result.Success(result.body()!!.retCode == APIResponse.RESPONSE_OK)
        }else{
            Result.Error(Exception(result.body()!!.retMessage))
        }
    }

    fun getPicture(wareID: Int, token : String) : Result<Bitmap> {
        val service = retrofit.create(WareService::class.java)


        val result = service.getPicture(
            GetPictureRequest(token = token, towID = wareID)
        ).execute()

        return if (result.body()!!.retCode == APIResponse.RESPONSE_OK){
            Result.Success(result.body()!!.picture!!)
        }else{
            Result.Error(Exception(result.body()!!.retMessage))
        }
    }


}