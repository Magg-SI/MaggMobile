package pl.tysia.maggstone

import android.content.Context
import android.content.DialogInterface

fun okDialog(title: String, message: String, context: Context) {
    val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(context)
    builder.setTitle(title)
    builder.setMessage(message)
        .setCancelable(false)
        .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, id ->
            //do things
        })
    val alert: android.app.AlertDialog? = builder.create()
    alert?.show()
}