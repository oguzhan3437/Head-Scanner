package com.oguzhancetin.goodpostureapp

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseDetector
import com.google.mlkit.vision.pose.PoseLandmark

class PoseDetectionProcess(
    val poseDetector: PoseDetector,
    val canvas: Canvas,
    val paint: Paint,
    val bitmap: Bitmap,
    val view: View,

) {



    fun processPose(onFailedListener: (messagge: String?) -> Unit) {

        poseDetector.process(InputImage.fromBitmap(bitmap, 0))
            .addOnSuccessListener { pose ->
                DisplayAll(pose)
                view.invalidate()
            }

            .addOnFailureListener { result ->
                Log.e("ml", "error")
                onFailedListener.invoke(result.message)
            }


    }

    private fun DisplayAll(pose: Pose) {

        try {
            val leftSholder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER)
            val rightSholder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER)
            val rightEar = pose.getPoseLandmark(PoseLandmark.RIGHT_EAR)
            val leftEar = pose.getPoseLandmark(PoseLandmark.LEFT_EAR)


            val leftSholderLandMarks: Pair<Float, Float> =
                Pair(leftSholder.position.x, leftSholder.position.y)
            val rightSholderLandMarks: Pair<Float, Float> =
                Pair(rightSholder.position.x, rightSholder.position.y)
            val leftEarLandMarks: Pair<Float, Float> = Pair(leftEar.position.x, leftEar.position.y)
            val rightEarLandMarks: Pair<Float, Float> =
                Pair(rightEar.position.x, rightEar.position.y)




            canvas.drawLine(
                leftSholderLandMarks.first, leftSholderLandMarks.second,
                leftEarLandMarks.first, leftEarLandMarks.second,
                paint
            )
        }catch (e:Exception){
            Log.e("exception Pose",e.stackTraceToString())
        }


    }
}