package com.oguzhancetin.goodpostureapp

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.drawable.Drawable
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.PoseLandmark
import com.google.mlkit.vision.pose.accurate.AccuratePoseDetectorOptions
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import android.media.tv.TvContract
import android.view.View
import android.widget.ImageView
import androidx.camera.view.PreviewView
import androidx.core.graphics.PaintCompat
import com.bumptech.glide.Glide
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import android.graphics.BitmapFactory

import android.graphics.Bitmap
import java.io.ByteArrayInputStream


class MainActivity : AppCompatActivity() {
    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var viewFinder: PreviewView
    private lateinit var imageView : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewFinder = findViewById<PreviewView>(R.id.viewFinder)
        imageView = findViewById<ImageView>(R.id.imageView)

        outputDirectory = getOutputDirectory()
        cameraExecutor = Executors.newSingleThreadExecutor()




        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }
        camera_capture_button.setOnClickListener { takePhoto() }


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(
                    this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }

    }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdir() }
        }

        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir

    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }


    private fun takePhoto() {

        val imageCapture = imageCapture ?: return
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(FILENAME_FORMAT, Locale.US)
                .format(System.currentTimeMillis()) + ".jpg"
        )
        val outputOption = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture.takePicture(
            outputOption,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    val msg = "Photo capture succeeded: $savedUri"
                    setImageView(savedUri)
                    Toast.makeText(baseContext, msg, Toast.LENGTH_LONG).show()
                    Log.d(TAG, msg)
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exception.message}", exception)
                }

            }
        )
    }
    private fun setImageView(uri:Uri?){
        if(viewFinder.visibility == View.VISIBLE){
            viewFinder.visibility = View.GONE
            imageView.visibility = View.VISIBLE


            val drawBitmap = Bitmap.createBitmap(
                imageView.width,
                imageView.height,
                Bitmap.Config.ARGB_8888
            )
            var bitmap = getResizedBitmap(BitmapFactory.decodeFile(uri?.encodedPath),imageView.width,imageView.height)

            Glide.with(this).load(drawBitmap).into(imageView)
            var paint = Paint().apply { this.color = Color.RED }
            var canvas = Canvas(drawBitmap)
            canvas.drawBitmap(bitmap,0f, 0f, null)


            imageView.invalidate()



            val poseDetecionOpt = AccuratePoseDetectorOptions.Builder()
                .setDetectorMode(AccuratePoseDetectorOptions.SINGLE_IMAGE_MODE)
                .build()
            val poseDetector = PoseDetection.getClient(poseDetecionOpt)
            val result: Task<Pose> = poseDetector.process(InputImage.fromBitmap(bitmap, 0))
                .addOnSuccessListener { pose ->
                    processPose(pose,canvas,paint,imageView)
                }

                .addOnFailureListener { result ->
                    Log.e("ml", "error")
                }



        }


    }
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

        Log.e("Original   dimensions", width.toString() + " " +height)
        Log.e("Compressed dimensions", decoded.width.toString() + " " + decoded.height)
        return decoded
    }

    private fun startCamera() {
        //pose detection innitiliazed
        /*
        val poseDetecionOpt = AccuratePoseDetectorOptions.Builder()
            .setDetectorMode(AccuratePoseDetectorOptions.SINGLE_IMAGE_MODE)
            .build()
        val poseDetector = PoseDetection.getClient(poseDetecionOpt)
         */
        //analyzer
        val imageAnalyzer = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
            .also {
                it.setAnalyzer(cameraExecutor, MyImageAnalyzer { inputImage ->
                    /*
                     *
                     */
                })
            }





        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener( Runnable{
            //preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewFinder.surfaceProvider)
                }

            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            imageCapture = ImageCapture.Builder()
                .build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview,imageCapture)

            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))

    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private const val TAG = "CameraXBasic"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS =
            arrayOf(Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE)
    }


    private fun processPose(pose: Pose, canvas: Canvas,paint: Paint,view:View) {
        try {

            val leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER)
            val rightShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER)

            val leftElbow = pose.getPoseLandmark(PoseLandmark.LEFT_ELBOW)
            val rightElbow = pose.getPoseLandmark(PoseLandmark.RIGHT_ELBOW)

            val leftWrist = pose.getPoseLandmark(PoseLandmark.LEFT_WRIST)
            val rightWrist = pose.getPoseLandmark(PoseLandmark.RIGHT_WRIST)


            val leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP)
            val rightHip = pose.getPoseLandmark(PoseLandmark.RIGHT_HIP)


            val leftKnee = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE)
            val rightKnee = pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE)


            val leftAnkle = pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE)
            val rightAnkle = pose.getPoseLandmark(PoseLandmark.RIGHT_ANKLE)

            val leftEye = pose.getPoseLandmark(PoseLandmark.LEFT_EYE)
            val rightEye = pose.getPoseLandmark(PoseLandmark.RIGHT_EYE)


            val leftEyePosition = leftEye.position
            val lEyeX = leftEyePosition.x
            val lEyeY = leftEyePosition.y
            val rightEyePosition = rightEye.position
            val rEyeX = rightEyePosition.x
            val rEyeY = rightEyePosition.y

            val leftShoulderP = leftShoulder.position
            val lShoulderX = leftShoulderP.x
            val lShoulderY = leftShoulderP.y
            val rightSoulderP = rightShoulder.position
            val rShoulderX = rightSoulderP.x
            val rShoulderY = rightSoulderP.y

            val leftElbowP = leftElbow.position
            val lElbowX = leftElbowP.x
            val lElbowY = leftElbowP.y
            val rightElbowP = rightElbow.position
            val rElbowX = rightElbowP.x
            val rElbowY = rightElbowP.y

            val leftWristP = leftWrist.position
            val lWristX = leftWristP.x
            val lWristY = leftWristP.y
            val rightWristP = rightWrist.position
            val rWristX = rightWristP.x
            val rWristY = rightWristP.y

            val leftHipP = leftHip.position
            val lHipX = leftHipP.x
            val lHipY = leftHipP.y
            val rightHipP = rightHip.position
            val rHipX = rightHipP.x
            val rHipY = rightHipP.y

            val leftKneeP = leftKnee.position
            val lKneeX = leftKneeP.x
            val lKneeY = leftKneeP.y
            val rightKneeP = rightKnee.position
            val rKneeX = rightKneeP.x
            val rKneeY = rightKneeP.y

            val leftAnkleP = leftAnkle.position
            val lAnkleX = leftAnkleP.x
            val lAnkleY = leftAnkleP.y
            val rightAnkleP = rightAnkle.position
            val rAnkleX = rightAnkleP.x
            val rAnkleY = rightAnkleP.y


            DisplayAll(
                lShoulderX, lShoulderY, rShoulderX, rShoulderY,
                lElbowX, lElbowY, rElbowX, rElbowY,
                lWristX, lWristY, rWristX, rWristY,
                lHipX, lHipY, rHipX, rHipY,
                lKneeX, lKneeY, rKneeX, rKneeY,
                lAnkleX, lAnkleY, rAnkleX, rAnkleY, canvas,paint,view ,lEyeX, lEyeY, rEyeX, rEyeY
            )
        } catch (e: Exception) {
            Toast.makeText(this@MainActivity, "Pose Landmarks failed", Toast.LENGTH_SHORT).show()
        }
    }
    private fun DisplayAll(
        lShoulderX: Float, lShoulderY: Float, rShoulderX: Float, rShoulderY: Float,
        lElbowX: Float, lElbowY: Float, rElbowX: Float, rElbowY: Float,
        lWristX: Float, lWristY: Float, rWristX: Float, rWristY: Float,
        lHipX: Float, lHipY: Float, rHipX: Float, rHipY: Float,
        lKneeX: Float, lKneeY: Float, rKneeX: Float, rKneeY: Float,
        lAnkleX: Float, lAnkleY: Float, rAnkleX: Float, rAnkleY: Float, canvas: Canvas,paint:Paint,view:View,
        lEyeX: Float, lEyeY: Float, rEyeX: Float, rEyeY: Float

    ) {



        canvas.drawLine(lEyeX, lEyeY, rEyeX, rEyeY, paint)

        canvas.drawLine(lShoulderX, lShoulderY, rShoulderX, rShoulderY, paint)

        canvas.drawLine(rShoulderX, rShoulderY, rElbowX, rElbowY, paint)

        canvas.drawLine(rElbowX, rElbowY, rWristX, rWristY, paint)

        canvas.drawLine(lShoulderX, lShoulderY, lElbowX, lElbowY, paint)

        canvas.drawLine(lElbowX, lElbowY, lWristX, lWristY, paint)

        canvas.drawLine(rShoulderX, rShoulderY, rHipX, rHipY, paint)

        canvas.drawLine(lShoulderX, lShoulderY, lHipX, lHipY, paint)

        canvas.drawLine(lHipX, lHipY, rHipX, rHipY, paint)

        canvas.drawLine(rHipX, rHipY, rKneeX, rKneeY, paint)

        canvas.drawLine(lHipX, lHipY, lKneeX, lKneeY, paint)

        canvas.drawLine(rKneeX, rKneeY, rAnkleX, rAnkleY, paint)

        canvas.drawLine(lKneeX, lKneeY, lAnkleX, lAnkleY, paint)
        view.invalidate()


    }



}


private class MyImageAnalyzer(private val imageListener: (Image) -> Unit) :
    ImageAnalysis.Analyzer {

    @androidx.camera.core.ExperimentalGetImage
    override fun analyze(image: ImageProxy) {

        val mediaImage = image.image
        if (mediaImage != null) {
            imageListener(mediaImage)

            val bitmap = mediaImage.toBitmap()
            bitmap?.let {
                val drawBitmap = Bitmap.createBitmap(
                    bitmap.getWidth(),
                    bitmap.getHeight(),
                    bitmap.getConfig()
                )

                var paint = Paint().apply { this.color = Color.RED }

                var canvas = Canvas(drawBitmap)
                canvas.drawBitmap(bitmap,0f, 0f, null)
                canvas.drawRect(Rect(100,100,200,200),paint)
            }






            mediaImage.close()

        }

    }
    fun Image?.toBitmap(): Bitmap? {
        this?.apply {
            val buffer = planes[0].buffer
            buffer.rewind()
            val bytes = ByteArray(buffer.capacity())
            buffer.get(bytes)
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        }
        return null

    }

}