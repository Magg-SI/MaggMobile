package pl.tysia.maggstone.ui

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build

import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import android.net.Uri
import android.os.Environment
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_picture_editor.*
import pl.tysia.maggstone.R
import pl.tysia.maggstone.data.model.Ware
import pl.tysia.maggstone.rotateBitmap
import pl.tysia.maggstone.ui.presentation_logic.EditPictureView
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*



class PictureEditorActivity : AppCompatActivity() {
    var currentPhotoPath: String? = null
    private lateinit var ware : Ware

    companion object{
        private const val MY_CAMERA_REQUEST_CODE = 100
        private const val REQUEST_TAKE_PHOTO = 1
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_picture_editor)

        ware = intent.getSerializableExtra(Ware.WARE_EXTRA) as Ware
        product_name.text = ware.name
        product_index.text = ware.index


        setZoom(scale_button);

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
        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, Uri.fromFile(file))

        val rotatedBitmap = rotateBitmap(bitmap)

        product_image.bitmap = rotatedBitmap
        // product_image.setImageBitmap(rotatedBitmap)

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
            "png_${timeStamp}_", /* prefix */
            ".png", /* suffix */
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
        // Store values at the time of the login attempt.
        var bitmap : Bitmap? = null
        if (product_image.bitmap != null)
            bitmap  = product_image.bitmap

        var cancel = false
        val focusView: View? = null

        // Check for a valid password, if the user entered one.
        if (bitmap == null){
            Toast.makeText(this, getString(R.string.picture_required_order), Toast.LENGTH_LONG).show()
            cancel = true
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView?.requestFocus()
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true)

            ware.photoPath = currentPhotoPath
            ware.imageBitmap = bitmap

        }
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
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
