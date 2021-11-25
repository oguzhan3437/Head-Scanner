package com.oguzhancetin.goodpostureapp

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.*
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
import android.view.View
import android.widget.ImageView
import androidx.camera.view.PreviewView
import com.bumptech.glide.Glide
import java.io.ByteArrayOutputStream
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

            var leftSholder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER)
            var rightSholder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER)
            var rightEar = pose.getPoseLandmark(PoseLandmark.RIGHT_EAR)
            var leftEar = pose.getPoseLandmark(PoseLandmark.LEFT_EAR)


            val leftSholderLandMarks: Pair<Float,Float> = Pair(leftSholder.position.x,leftSholder.position.y)
            val rightSholderLandMarks: Pair<Float,Float> = Pair(rightSholder.position.x,rightSholder.position.y)
            val leftEarLandMarks: Pair<Float,Float> = Pair(leftEar.position.x,leftEar.position.y)
            val rightEarLandMarks: Pair<Float,Float> = Pair(rightEar.position.x,rightEar.position.y)





            DisplayAll(leftSholderLandMarks,rightSholderLandMarks,leftEarLandMarks,rightEarLandMarks,canvas,paint,view)
        } catch (e: Exception) {
            Toast.makeText(this@MainActivity, "Pose Landmarks failed", Toast.LENGTH_SHORT).show()
        }
    }
    private fun   DisplayAll(
        leftSholderLandMarks:Pair<Float,Float>,
        rightSholderLandMarks:Pair<Float,Float>,
        leftEarLandMarks:Pair<Float,Float>,
        rightEarLandMarks:Pair<Float,Float>,
        canvas:Canvas,
        paint:Paint,
        view:View) {



        canvas.drawLine(
            leftSholderLandMarks.first,leftSholderLandMarks.second,
            leftEarLandMarks.first,leftEarLandMarks.second,
            paint
        )



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