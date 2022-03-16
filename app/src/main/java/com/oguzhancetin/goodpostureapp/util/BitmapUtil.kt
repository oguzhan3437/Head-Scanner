package com.oguzhancetin.goodpostureapp.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.util.Log
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

fun getResizedBitmap(bm: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
    val width = bm.width
    val height = bm.height
    val scaleWidth = newWidth.toFloat() / width
    val scaleHeight = newHeight.toFloat() / height
    // CREATE A MATRIX FOR THE MANIPULATION
    val matrix = Matrix()
    // RESIZE THE BIT MAP
    matrix.postScale(scaleWidth, scaleHeight)

    // "RECREATE" THE NEW BITMAP
    val resizedBitmap = Bitmap.createBitmap(
        bm, 0, 0, width, height, matrix, false
    )
    bm.recycle()
    return resizedBitmap
}

private fun Bitmap.compress(): Bitmap? {
    val out = ByteArrayOutputStream()
    compress(Bitmap.CompressFormat.JPEG, 100, out)
    val decoded = BitmapFactory.decodeStream(ByteArrayInputStream(out.toByteArray()))

    Log.e("Original   dimensions", width.toString() + " " + height)
    Log.e("Compressed dimensions", decoded.width.toString() + " " + decoded.height)
    return decoded
}