package pl.tysia.maggstone.ui.picture

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.IBinder
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_picture_editor.*
import pl.tysia.maggstone.R
import pl.tysia.maggstone.app.MaggApp
import pl.tysia.maggstone.data.model.Ware
import pl.tysia.maggstone.data.service.SendingService
import pl.tysia.maggstone.resizeBitmap
import pl.tysia.maggstone.rotateBitmap
import pl.tysia.maggstone.ui.BaseActivity
import pl.tysia.maggstone.ui.presentation_logic.EditPictureView
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class PictureEditorActivity : BaseActivity() {
    private var currentPhotoPath: String? = null
    private lateinit var ware : Ware
    private lateinit var mService: SendingService
    private var mBound: Boolean = false

    @Inject lateinit var viewModel: PictureViewModel

    companion object{
        private const val MY_CAMERA_REQUEST_CODE = 100
        private const val REQUEST_TAKE_PHOTO = 1
    }

    /** Defines callbacks for service binding, passed to bindService()  */
    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as SendingService.SendingBinder
            mService = binder.getService()
            mBound = true

        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as MaggApp).appComponent.inject(this)

        setContentView(R.layout.activity_picture_editor)

        ware = intent.getSerializableExtra(Ware.WARE_EXTRA) as Ware
        product_name.text = ware.name
        product_index.text = ware.index


        setZoom(scale_button);

        viewModel.pictureResult.observe(this@PictureEditorActivity, Observer {
            Toast.makeText(this, getString(it), Toast.LENGTH_LONG).show()
            imageProgressBar.visibility = View.INVISIBLE
            edit_button.isEnabled = true
        })


        viewModel.picture.observe(this@PictureEditorActivity, Observer {
            product_image.bitmap = it
            imageProgressBar.visibility = View.INVISIBLE
            edit_button.isEnabled = true
        })

        if (ware.photoID != null) {
            viewModel.getPicture(ware.photoID!!)
        }else{
            imageProgressBar.visibility = View.INVISIBLE
            edit_button.isEnabled = true
        }

        checkPermission()
    }

    private fun checkPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_DENIED){
            val arr = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            ActivityCompat.requestPermissions(this, arr , 6)
        }
    }


    fun onEditClicked(view: View){
        editingEnabled(true)
    }

    private fun editingEnabled(enabled : Boolean){
        if (enabled){
            editor_buttons.visibility = View.VISIBLE
            editor_layout.visibility = View.VISIBLE
            edit_button.visibility = View.INVISIBLE
        }else{
            editor_buttons.visibility = View.INVISIBLE
            editor_layout.visibility = View.INVISIBLE
            edit_button.visibility = View.VISIBLE

            product_image.setMode(EditPictureView.MODE_SCALE)
        }
    }

    override fun onStop() {
        super.onStop()
        if (mBound){
            unbindService(connection)
            mBound = false
        }

    }

    fun onRotateClicked(view: View){
        val bitmap = product_image.bitmap
        val rotated = rotateBitmap(bitmap)
        product_image.bitmap = rotated
    }

    fun setCrop(view : View){
        product_image.setMode(EditPictureView.MODE_CROP)
        crop_button.isActivated = true
        scale_button.isActivated = false
    }

    fun setZoom(view : View){
        product_image.setMode(EditPictureView.MODE_SCALE)
        crop_button.isActivated = false
        scale_button.isActivated = true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }

        return true

    }

    private fun setProductImageBitmap(path : String?){
        val file = File(path)
        val uri = Uri.fromFile(file)

        var bitmap = MediaStore.Images.Media.getBitmap(
            this.contentResolver,
            uri
        )

        bitmap = resizeBitmap(bitmap, getPictureSize())

        product_image.bitmap = bitmap

    }

    private fun getPictureSize() : Float{
        return PreferenceManager
            .getDefaultSharedPreferences(this)
            .getString("picture_size", "0.1")!!.toFloat()
    }

    fun takePicture(view : View){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_DENIED){
            val arr = arrayOf(Manifest.permission.CAMERA)
            ActivityCompat.requestPermissions(this, arr , 6)

        }else{
            startActivityForResult(Intent(this@PictureEditorActivity, TakePictureActivity::class.java), REQUEST_TAKE_PHOTO)
            //dispatchTakePictureIntent()
            //cameraTest()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent()
            }
        }
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


    private fun dispatchTakePictureIntent() {
        //getPhotoTask?.cancel(true)

        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    ex.printStackTrace()
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "pl.tysia.maggstone.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    startActivityForResult(takePictureIntent,
                        REQUEST_TAKE_PHOTO
                    )
                }
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            val path = data!!.extras!!.getString("Photo_path")
            setProductImageBitmap(path)
        }
    }


    fun attemptSave(view : View) {
        showBlockingLoading(true)

        var bitmap : Bitmap? = null
        if (product_image.bitmap != null)
            bitmap  = product_image.bitmap

        var cancel = false
        val focusView: View? = null

        if (bitmap == null){
            Toast.makeText(this, getString(R.string.picture_required_order), Toast.LENGTH_LONG).show()
            cancel = true
        }

        if (!cancel) {

            if(currentPhotoPath == null) createImageFile()

            val file = File(currentPhotoPath!!)
            val out = FileOutputStream(file, false)
            bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, out)

            ware.photoPath = currentPhotoPath

            if (!mService.isRunning) startService(Intent(this, SendingService::class.java))

            mService.addToQueue(ware).percentSent.observe(this, Observer {
                sending_progress_bar.progress = it
            })

            product_image.bitmap = bitmap

            editingEnabled(false)

        }
        showBlockingLoading(false)

    }



    override fun onStart() {
        super.onStart()
        Intent(this, SendingService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

}
