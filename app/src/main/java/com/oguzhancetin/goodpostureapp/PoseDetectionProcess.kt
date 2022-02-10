package com.oguzhancetin.goodpostureapp

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.Log
import android.view.View
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseDetector
import com.google.mlkit.vision.pose.PoseLandmark
import kotlin.math.atan2

sealed class ProcessResult(message:String?){
class ProcessSucces(val message:String?): ProcessResult(message)
class ProcessError(val message:String?): ProcessResult(message)
}


class PoseDetectionProcess(
    val poseDetector: PoseDetector,
    val canvas: Canvas,
    val paint: Paint,
    val bitmap: Bitmap,
    private val view: View,

    ) {



    fun processPose(resultCallback:(ProcessResult?) -> Unit ) {

        poseDetector.process(InputImage.fromBitmap(bitmap, 0))
            .addOnSuccessListener { pose ->
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



                    val pairEar = Pair(leftEarLandMarks.first,leftEarLandMarks.second)
                    val pairShoulder = Pair(leftSholderLandMarks.first,leftSholderLandMarks.second)
                    val pairIntersection = Pair(leftSholderLandMarks.first,leftEarLandMarks.second)




                    canvas.drawLine(
                        leftSholderLandMarks.first, leftSholderLandMarks.second,
                        leftEarLandMarks.first, leftEarLandMarks.second,
                        paint
                    )
                    canvas.drawPoint(
                        leftSholderLandMarks.first,leftEarLandMarks.second,
                        paint
                    )
                    canvas.drawLine(
                        leftSholderLandMarks.first, leftEarLandMarks.second,
                        leftSholderLandMarks.first, leftSholderLandMarks.second,
                        paint
                    )
                    val angle = getAngle(pairIntersection,pairShoulder,pairEar)
                    canvas.drawText("angle : "+(90-angle),200f,50f,paint)



                }catch (e:Exception){
                    Log.e("exception Pose",e.stackTraceToString())
                }

                resultCallback.invoke(ProcessResult.ProcessSucces("Başarılı"))

            }
            .addOnFailureListener { result ->
                resultCallback.invoke(ProcessResult.ProcessError(result.message))
            }


    }

    private fun DisplayAll(pose: Pose) {



    }
    private fun getAngle(firstPoint: Pair<Float, Float>, midPoint: Pair<Float, Float>, lastPoint: Pair<Float, Float>): Double {
        var result = Math.toDegrees(
            (atan2(lastPoint.second - midPoint.second,
                lastPoint.first - midPoint.second)
                    - atan2(firstPoint.second - midPoint.second,
                firstPoint.first- midPoint.first)).toDouble()
        )
        result = Math.abs(result) // Angle should never be negative
        if (result > 180) {
            result = 360.0 - result // Always get the acute representation of the angle
        }
        return result
    }


}