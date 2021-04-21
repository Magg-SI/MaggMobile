package pl.tysia.maggstone

import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Base64
import java.io.ByteArrayOutputStream

fun rotateBitmap(bitmap : Bitmap?) : Bitmap?{
    if (bitmap!=null){
        val matrix = Matrix()
        matrix.postRotate(270f)

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)

    }

    return null
}

fun resizeBitmap(bitmap:Bitmap, percent : Float) : Bitmap{
    val width = (bitmap.width * percent).toInt()
    val height = (bitmap.height * percent).toInt()
    return Bitmap.createScaledBitmap(bitmap, width , height , true)

}

fun getPhotoString(bitmap : Bitmap, pictureSize : Float) : String{
    val stream = ByteArrayOutputStream()
    val resized = resizeBitmap(bitmap, pictureSize)
    resized.compress(Bitmap.CompressFormat.JPEG, 100, stream)
    val image = stream.toByteArray()
    return Base64.encodeToString(image, Base64.DEFAULT)
}

