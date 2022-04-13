package com.oguzhancetin.goodpostureapp

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import androidx.camera.core.AspectRatio
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import com.google.mlkit.vision.pose.PoseLandmark
import java.io.File
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.max
import kotlin.math.min


private const val RATIO_4_3_VALUE = 4.0 / 3.0
private const val RATIO_16_9_VALUE = 16.0 / 9.0
fun getOutputDirectory(application: Application): File {
    application.apply {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name1).trim()).apply {
                mkdir()
            }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
    }
}

fun getAngle(
    pair: Pair<Float, Float>,
    midPoint: PoseLandmark,
    lastPoint: PoseLandmark
): Double {
    var result = Math.toDegrees(
        (atan2(
            lastPoint.position.y - midPoint.position.y,
            lastPoint.position.x - midPoint.position.x
        )
                - atan2(
            pair.second - midPoint.getPosition().y,
            pair.first - midPoint.position.x
        )).toDouble()
    )
    result = Math.abs(result) // Angle should never be negative
    if (result > 180) {
        result = 360.0 - result // Always get the acute representation of the angle
    }
    return result
}

fun aspectRatio(width: Int, height: Int): Int {
    val previewRatio = max(width, height).toDouble() / min(width, height)
    if (abs(previewRatio - RATIO_4_3_VALUE) <= abs(previewRatio - RATIO_16_9_VALUE)) {
        return AspectRatio.RATIO_4_3
    }
    return AspectRatio.RATIO_16_9
    // return AspectRatio.RATIO_4_3
}


fun rotateImageWithUri(uri: Uri): Bitmap {
    val ei = ExifInterface(uri.encodedPath!!)
    val orientation: Int = ei.getAttributeInt(
        ExifInterface.TAG_ORIENTATION,
        ExifInterface.ORIENTATION_UNDEFINED
    )
    val b1 = BitmapFactory.decodeFile(uri.encodedPath)
    val rotatedBitmap = when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> TransformationUtils.rotateImage(b1, 90)
        ExifInterface.ORIENTATION_ROTATE_180 -> TransformationUtils.rotateImage(b1, 180)
        ExifInterface.ORIENTATION_ROTATE_270 -> TransformationUtils.rotateImage(b1, 270)
        ExifInterface.ORIENTATION_NORMAL -> b1
        else -> b1
    }
    return rotatedBitmap

}