package com.oguzhancetin.goodpostureapp.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.Log
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseDetector
import com.google.mlkit.vision.pose.PoseLandmark
import com.oguzhancetin.goodpostureapp.getAngle

sealed class ProcessResult(message: String?) {
    class ProcessSuccess(val degree: String?) : ProcessResult(degree)
    class ProcessError(val message: String?) : ProcessResult(message)
    class ProcessLoading() : ProcessResult(null)
}

class PoseDetectionProcess(
    private val poseDetector: PoseDetector,
    private val canvas: Canvas,
    private val paint1: Paint,
    private val paint2: Paint,
    private val bitmap: Bitmap,
) {
    //start image processing
    fun processPose(resultCallback: (ProcessResult?) -> Unit) {
        poseDetector.process(InputImage.fromBitmap(bitmap, 0))
            .addOnSuccessListener { pose ->
                resultCallback.invoke(
                    ProcessResult.ProcessSuccess(
                        findNeckDegree(pose)?.toString()
                    )
                )
            }
            .addOnFailureListener { result ->
                resultCallback.invoke(ProcessResult.ProcessError(result.message))
            }
    }

    private fun findNeckDegree(pose: Pose): Int? {
        try {
            val leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER)!!
            val leftEar = pose.getPoseLandmark(PoseLandmark.LEFT_EAR)!!
            val leftShoulderLandMarks: Pair<Float, Float> =
                Pair(leftShoulder.position.x, leftShoulder.position.y)
            val leftEarLandMarks: Pair<Float, Float> =
                Pair((leftEar.position.x + 20f), (leftEar.position.y + 30f))
            val pairIntersection = Pair(leftShoulderLandMarks.first, leftEarLandMarks.second)
            val angle = getAngle(pairIntersection, leftShoulder, leftEar)

            //draw everything needed
            canvas.apply {
                drawLine(
                    leftShoulderLandMarks.first, leftShoulderLandMarks.second,
                    leftEarLandMarks.first, leftEarLandMarks.second,
                    paint1
                )
                drawLine(
                    leftShoulderLandMarks.first, leftShoulderLandMarks.second + 170f,
                    leftShoulderLandMarks.first, leftEarLandMarks.second - 170f,
                    paint2
                )
                drawCircle(leftEarLandMarks.first, leftEarLandMarks.second, 25f, paint1)
                drawCircle(leftShoulderLandMarks.first, leftShoulderLandMarks.second, 15f, paint1)
            }
            return angle.toInt()
        } catch (e: Exception) {
            Log.e("exception Pose", e.stackTraceToString())
        }
        return null
    }
}