package com.oguzhancetin.goodpostureapp

import android.app.Application
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.AspectRatio
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.mlkit.vision.pose.PoseLandmark
import com.oguzhancetin.goodpostureapp.databinding.FragmentShowResultDialogBinding
import com.oguzhancetin.goodpostureapp.fragment.BaseFragment
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
}
