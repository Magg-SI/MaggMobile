package pl.tysia.maggstone.ui.picture

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.IBinder
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.util.Base64
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_picture_editor.*
import pl.tysia.maggstone.R
import pl.tysia.maggstone.data.NetAddressManager
import pl.tysia.maggstone.data.source.LoginDataSource
import pl.tysia.maggstone.data.source.LoginRepository
import pl.tysia.maggstone.data.model.Ware
import pl.tysia.maggstone.data.service.SendingService
import pl.tysia.maggstone.data.service.WaresDownloadService
import pl.tysia.maggstone.resizeBitmap
import pl.tysia.maggstone.rotateBitmap
import pl.tysia.maggstone.ui.ViewModelFactory
import pl.tysia.maggstone.ui.presentation_logic.EditPictureView
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class PictureEditorActivity : AppCompatActivity() {
    private var currentPhotoPath: String? = null
    private lateinit var ware : Ware
    private lateinit var mService: SendingService
    private var mBound: Boolean = false

    private lateinit var viewModel: PictureViewModel

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

        setContentView(R.layout.activity_picture_editor)

        ware = intent.getSerializableExtra(Ware.WARE_EXTRA) as Ware
        product_name.text = ware.name
        product_index.text = ware.index


        setZoom(scale_button);

        viewModel = ViewModelProvider(this,
            ViewModelFactory(this)
        ).get(PictureViewModel::class.java)


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

        val token = LoginRepository(
            LoginDataSource(NetAddressManager(this)),
            this
        ).user!!.token

        viewModel.getPicture(ware.id!!, token)

    }

    fun onEditClicked(view: View){
        editingEnabled(true)
    }

    private fun editingEnabled(enabled : Boolean){
        if (enabled){
            editor_buttons.visibility = View.VISIBLE
            editor_layout.visibility = View.VISIBLE
            edit_button.visibility = View.GONE
        }else{
            editor_buttons.visibility = View.GONE
            editor_layout.visibility = View.GONE
            edit_button.visibility = View.VISIBLE

            //TODO: pozbyć się magicznych cyferek
            product_image.setMode(1)
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

        val bitmap = MediaStore.Images.Media.getBitmap(
            this.contentResolver,
            uri
        )

        product_image.bitmap = bitmap

    }

    fun takePicture(view : View){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_DENIED){
            val arr = arrayOf(Manifest.permission.CAMERA)
            ActivityCompat.requestPermissions(this, arr , 6)

        }else{
            dispatchTakePictureIntent()
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

        return File.createTempFile(
            "jpeg_${timeStamp}_", /* prefix */
            ".jpeg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
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
                    // Error occurred while creating the File
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
            setProductImageBitmap(currentPhotoPath)
        }
    }


    fun attemptSave(view : View) {
        showProgress(true)

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
        showProgress(false)

    }



    override fun onStart() {
        super.onStart()
        Intent(this, SendingService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }


    private fun showProgress(show: Boolean) {
        val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

        ware_edit_form.visibility = if (show) View.GONE else View.VISIBLE
        ware_edit_form.animate()
            .setDuration(shortAnimTime)
            .alpha((if (show) 0 else 1).toFloat())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    ware_edit_form.visibility = if (show) View.GONE else View.VISIBLE
                }
            })

        ware_editor_progress_bar.visibility = if (show) View.VISIBLE else View.GONE
        ware_editor_progress_bar.animate()
            .setDuration(shortAnimTime)
            .alpha((if (show) 1 else 0).toFloat())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    ware_editor_progress_bar.visibility = if (show) View.VISIBLE else View.GONE
                }
            })
    }


}
