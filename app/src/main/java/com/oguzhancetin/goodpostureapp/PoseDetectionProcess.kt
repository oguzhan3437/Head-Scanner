package com.oguzhancetin.goodpostureapp

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseDetector
import com.google.mlkit.vision.pose.PoseLandmark
import kotlin.math.atan2

sealed class ProcessResult(message: String?) {
    class ProcessSucces(val degree: String?) : ProcessResult(degree)
    class ProcessError(val message: String?) : ProcessResult(message)
}


class PoseDetectionProcess(
    val poseDetector: PoseDetector,
    val canvas: Canvas,
    val paint1: Paint,
    val paint2: Paint,
    val bitmap: Bitmap,
    private val view: View,

    ) {


    fun processPose(resultCallback: (ProcessResult?) -> Unit) {

        poseDetector.process(InputImage.fromBitmap(bitmap, 0))
            .addOnSuccessListener { pose ->
                resultCallback.invoke(
                    ProcessResult.ProcessSucces(
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
            val leftSholder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER)!!
            val rightSholder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER)!!
            val rightEar = pose.getPoseLandmark(PoseLandmark.RIGHT_EAR)!!
            val leftEar = pose.getPoseLandmark(PoseLandmark.LEFT_EAR)!!


            val leftSholderLandMarks: Pair<Float, Float> =
                Pair(leftSholder.position.x, leftSholder.position.y)
            val rightSholderLandMarks: Pair<Float, Float> =
                Pair(rightSholder.position.x, rightSholder.position.y)
            val leftEarLandMarks: Pair<Float, Float> = Pair((leftEar.position.x+20f), (leftEar.position.y+30f))
            val rightEarLandMarks: Pair<Float, Float> =
                Pair(rightEar.position.x, rightEar.position.y)


            val pairEar = Pair(leftEarLandMarks.first, leftEarLandMarks.second)
            val pairShoulder = Pair(leftSholderLandMarks.first, leftSholderLandMarks.second)
            val pairIntersection = Pair(leftSholderLandMarks.first, leftEarLandMarks.second)


            canvas.drawLine(
                leftSholderLandMarks.first, leftSholderLandMarks.second,
                leftEarLandMarks.first, leftEarLandMarks.second,
                paint1
            )


            canvas.drawLine(
                leftSholderLandMarks.first, leftSholderLandMarks.second+170f,
                leftSholderLandMarks.first, leftEarLandMarks.second-170f,
                paint2
            )
            canvas.drawCircle(leftEarLandMarks.first,leftEarLandMarks.second,25f,paint1)
            canvas.drawCircle(leftSholderLandMarks.first,leftSholderLandMarks.second,15f,paint1)

            val angle = getAngle(pairIntersection, leftSholder, leftEar)



            canvas.drawText("angle : " + angle, 200f, 50f, paint1)

            return angle.toInt()

        } catch (e: Exception) {
            Log.e("exception Pose", e.stackTraceToString())
        }

        return null
    }

    private fun getAngle(
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


}