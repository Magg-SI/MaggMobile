package pl.tysia.maggstone.ui.scanner

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import kotlinx.android.synthetic.main.activity_scanning.*
import pl.tysia.maggstone.ui.BaseActivity
import java.util.concurrent.Executors

abstract class ScanningActivity : BaseActivity(),  ImageAnalysis.Analyzer {
    private lateinit var cameraProviderFuture : ListenableFuture<ProcessCameraProvider>
    private lateinit var scanner : BarcodeScanner
    private lateinit var cameraProvider : ProcessCameraProvider
    private lateinit var analyzerUseCase : ImageAnalysis
    private val executor = Executors.newSingleThreadExecutor()

    private var scanOnDemand = false
    private var isScanning = false

    protected var lastValue : Barcode? = null

    abstract fun setContentView()
    abstract fun onSuccess(barcode : Barcode)
    abstract fun isValid(barcode : Barcode) : Boolean

    override fun onCreate(savedInstanceState: Bundle?) {
        val theme = intent.getIntExtra("theme", -1)
        if (theme != -1) setTheme(theme)

        super.onCreate(savedInstanceState)
        setContentView()

        cameraProviderFuture = ProcessCameraProvider.getInstance(this)


        cameraProviderFuture.addListener(Runnable {
            cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider)
        }, ContextCompat.getMainExecutor(this))

        scanner = BarcodeScanning.getClient()

        scanOnDemand = PreferenceManager
            .getDefaultSharedPreferences(this)
            .getBoolean("scan_on_demand", false)

        if (scanOnDemand) {
            scanButton.visibility = View.VISIBLE
            scanButton.setOnClickListener { isScanning = true }
        }else{
            scanButton.visibility = View.GONE
        }

        checkPermission()
    }

    private fun checkPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_DENIED){
            val arr = arrayOf(Manifest.permission.CAMERA)
            ActivityCompat.requestPermissions(this, arr , 6)
        }
    }


    private fun bindPreview(cameraProvider : ProcessCameraProvider) {
        val preview : Preview = Preview.Builder()
            .build()

        val cameraSelector : CameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        preview.setSurfaceProvider(previewView.surfaceProvider)

        val analyzer = ImageAnalysis.Builder().build()
        analyzerUseCase = analyzer.apply {
            setAnalyzer(executor, this@ScanningActivity)
        }

        cameraProvider.bindToLifecycle(this, cameraSelector, analyzerUseCase, preview)


    }

    @SuppressLint("UnsafeExperimentalUsageError", "UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            val result = scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    if (barcodes.size > 1) {
                        code_view.text = "Znaleziono więcej niż jeden kod"
                    }
                    else if (barcodes.size == 1) {
                        val barcode = barcodes[0]
                        code_view.text = barcode.rawValue
                        if ((isScanning || !scanOnDemand) && isValid(barcode)) {
                            onSuccess(barcode)
                            lastValue = barcode
                            isScanning = false
                        }
                    }
                }
                .addOnFailureListener {
                    val m = it.message
                    Toast.makeText(this, m, Toast.LENGTH_LONG).show()
                }
                .addOnCompleteListener {
                    mediaImage.close()
                    imageProxy.close()
                }

        }

    }

    protected fun showSendingState(state: Boolean) {
        scanner_progress.visibility = if (state) View.VISIBLE else View.INVISIBLE

        if (state)
            cameraProvider.unbindAll()
        else
            bindPreview(cameraProvider)

    }


}