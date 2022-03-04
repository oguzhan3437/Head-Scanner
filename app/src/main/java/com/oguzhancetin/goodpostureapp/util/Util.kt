package com.oguzhancetin.goodpostureapp

import android.app.Application
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.view.LayoutInflater
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File


fun getDataColumn(
    context: Context, uri: Uri?, selection: String?,
    selectionArgs: Array<String>?
): String? {
    var cursor: Cursor? = null
    val column = "_data"
    val projection = arrayOf(
        column
    )
    try {
        cursor = uri?.let {
            context.contentResolver.query(
                it, projection, selection, selectionArgs,
                null
            )
        }
        if (cursor != null && cursor.moveToFirst()) {
            val index: Int = cursor.getColumnIndexOrThrow(column)
            return cursor.getString(index)
        }
    } finally {
        if (cursor != null) cursor.close()
    }
    return null
}


fun getOutputDirectory(application:Application): File {
    application.apply {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply {
                mkdir()
            }
        }

        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
    }

}

fun showResultDialog(context: Context,onPositiviClick:()->Unit) {
    val customAlertDialogView = LayoutInflater.from(context)
        .inflate(R.layout.fragment_show_result_dialog, null, false)
    val materialDialog = MaterialAlertDialogBuilder(context,R.style.ThemeOverlay_App_MaterialAlertDialog)
    materialDialog.setView(customAlertDialogView)
        .setTitle("Save captured photo")
        .setPositiveButton("Add") { dialog, _ ->
            onPositiviClick.invoke()
            dialog.dismiss()
        }
        .setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        .show()

}
