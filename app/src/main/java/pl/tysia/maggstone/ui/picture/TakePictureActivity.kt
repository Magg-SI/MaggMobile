package pl.tysia.maggstone.ui.picture

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.android.synthetic.main.activity_scanning.*
import kotlinx.android.synthetic.main.activity_scanning.previewView
import kotlinx.android.synthetic.main.activity_take_picture.*
import pl.tysia.maggstone.R
import pl.tysia.maggstone.ui.BaseActivity
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors


class TakePictureActivity : BaseActivity() {
    private lateinit var cameraProviderFuture : ListenableFuture<ProcessCameraProvider>
    private lateinit var cameraProvider : ProcessCameraProvider
    private val executor = Executors.newSingleThreadExecutor()

    private lateinit var imageCapture : ImageCapture
    private var currentPhotoPath: String? = null
    private lateinit var camera: Camera


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_take_picture)

        cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener(Runnable {
            cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider)
        }, ContextCompat.getMainExecutor(this))


        previewView.setOnTouchListener(View.OnTouchListener setOnTouchListener@{ _: View, motionEvent: MotionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> return@setOnTouchListener true
                MotionEvent.ACTION_UP -> {
                    val factory = previewView.meteringPointFactory

                    val point = factory.createPoint(motionEvent.x, motionEvent.y)

                    val action = FocusMeteringAction.Builder(point).build()

                    camera.cameraControl.startFocusAndMetering(action)

                    return@setOnTouchListener true
                }
                else -> return@setOnTouchListener false
            }
        })

    }

    fun onTorchClicked(view: View){
        view.isActivated = !view.isActivated
        camera.cameraControl.enableTorch(view.isActivated)
    }


    fun onTakePictureClicked(view: View){
        val outputFileOptions = ImageCapture.OutputFileOptions.Builder(createImageFile()).build()

        imageView5.setImageBitmap(previewView.bitmap)
        imageCapture.takePicture(outputFileOptions, executor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(error: ImageCaptureException)
                {
                    imageView5.setImageBitmap(null)
                }
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val returnIntent = Intent()
                    returnIntent.putExtra("Photo_path", currentPhotoPath)
                    setResult(Activity.RESULT_OK, returnIntent)
                    finish()
                }
            })
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        val image = File.createTempFile(
            "jpeg_${timeStamp}_", /* prefix */
            ".jpeg", /* suffix */
            storageDir /* directory */
        )

        currentPhotoPath = image.absolutePath

        return image

    }


    private fun bindPreview(cameraProvider : ProcessCameraProvider) {
        imageCapture = ImageCapture.Builder()
           // .setTargetRotation(previewView.display.rotation)
            .build()

        val preview : Preview = Preview.Builder()
            .build()

        val cameraSelector : CameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        preview.setSurfaceProvider(previewView.surfaceProvider)


        camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)

        if (camera.cameraInfo.hasFlashUnit()) torchButton.visibility = View.VISIBLE
        else torchButton.visibility = View.GONE

    }

}

