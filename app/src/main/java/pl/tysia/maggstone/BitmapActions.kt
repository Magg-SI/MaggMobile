package pl.tysia.maggstone

import android.graphics.Bitmap
import android.graphics.Matrix

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

