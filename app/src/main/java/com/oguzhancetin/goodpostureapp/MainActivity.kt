package com.oguzhancetin.goodpostureapp

import android.Manifest
import android.app.Activity
import android.content.Intent
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
import android.provider.MediaStore
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import java.io.ByteArrayInputStream
import android.provider.DocumentsContract

import android.content.ContentUris
import android.content.Context
import android.database.Cursor

import android.os.Environment

import android.os.Build


class MainActivity : AppCompatActivity() {
    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var viewFinder: PreviewView
    private lateinit var imageView: ImageView

    val poseDetecionOpt = AccuratePoseDetectorOptions.Builder()
        .setDetectorMode(AccuratePoseDetectorOptions.SINGLE_IMAGE_MODE)
        .build()
    val poseDetector = PoseDetection.getClient(poseDetecionOpt)

   /* private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                it.data?.data.let { uri ->


                    //setImageView(path)

                }

            }


        }*/




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
        btn_clear.setOnClickListener { clearScreen() }

        //button_from_device.setOnClickListener { openSelectFile() }


    }

    private fun clearScreen() {
        viewFinder.visibility = View.VISIBLE
        imageView.visibility = View.INVISIBLE
    }

    /* private fun openSelectFile() {
         val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
         startForResult.launch(intent)


     }*/


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
            File(it, resources.getString(R.string.app_name)).apply {
                mkdir()
            }
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

    private fun setImageView(uri: Uri?) {
        if (viewFinder.visibility == View.VISIBLE) {
            viewFinder.visibility = View.GONE
            imageView.visibility = View.VISIBLE


            //empty bitmap with given size
            val drawBitmap = Bitmap.createBitmap(
                imageView.width,
                imageView.height,
                Bitmap.Config.ARGB_8888
            )
            // taken phote from camera
            var bitmap = getResizedBitmap(
                BitmapFactory.decodeFile(uri?.encodedPath),
                imageView.width,
                imageView.height
            )

            Glide.with(this).load(drawBitmap).into(imageView)
            var paint = Paint().apply { this.color = Color.RED }
            var canvas = Canvas(drawBitmap)
            canvas.drawBitmap(bitmap, 0f, 0f, null)

            imageView.invalidate()
            //pose detection process change bitmap reference so image change
            PoseDetectionProcess(poseDetector,canvas, paint,bitmap, imageView).processPose{
                    Toast.makeText(this,it,Toast.LENGTH_SHORT).show()
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

        Log.e("Original   dimensions", width.toString() + " " + height)
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
        cameraProviderFuture.addListener(Runnable {
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
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)

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
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
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
                canvas.drawBitmap(bitmap, 0f, 0f, null)
                canvas.drawRect(Rect(100, 100, 200, 200), paint)
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