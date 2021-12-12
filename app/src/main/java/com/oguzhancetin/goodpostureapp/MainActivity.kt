package com.oguzhancetin.goodpostureapp

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.*
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.accurate.AccuratePoseDetectorOptions
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import android.view.View

import com.bumptech.glide.Glide
import java.io.ByteArrayOutputStream
import android.graphics.BitmapFactory

import android.graphics.Bitmap
import java.io.ByteArrayInputStream

import com.oguzhancetin.goodpostureapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    private val _binding get() = binding!!
    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService

    val poseDetecionOpt = AccuratePoseDetectorOptions.Builder()
        .setDetectorMode(AccuratePoseDetectorOptions.SINGLE_IMAGE_MODE)
        .build()
    val poseDetector = PoseDetection.getClient(poseDetecionOpt)




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)


        outputDirectory = getOutputDirectory(application)
        cameraExecutor = Executors.newSingleThreadExecutor()

        _binding.cameraCaptureButton.setOnClickListener { takePhoto() }
        _binding.btnClear.setOnClickListener { clearScreen() }
        //button_from_device.setOnClickListener { openSelectFile() }

        checkPermisionsOk()

    }


    private fun checkPermisionsOk() {
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }
    }

    private fun clearScreen() {
        _binding.viewFinder.visibility = View.VISIBLE
        _binding.imageView.visibility = View.INVISIBLE
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



    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
        binding = null
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
                    //Uri.fromFile(photoFile)
                    val savedUri = Uri.fromFile(photoFile) //Uri.parse("file:///storage/emulated/0/Android/media/com.oguzhancetin.goodpostureapp/GoodPostureApp/2021-11-26-12-01-36-293.jpg") //
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
        if (_binding.viewFinder.visibility == View.VISIBLE) {
            _binding.viewFinder.visibility = View.GONE
            _binding.imageView.visibility = View.VISIBLE

            //empty bitmap with given size
            val drawBitmap = Bitmap.createBitmap(
                _binding.imageView.width,
                _binding.imageView.height,
                Bitmap.Config.ARGB_8888
            )
            // taken phote from camera
            var bitmap = getResizedBitmap(
                BitmapFactory.decodeFile(uri?.encodedPath),
                _binding.imageView.width,
                _binding.imageView.height
            )

            Glide.with(this).load(drawBitmap).into(_binding.imageView)
            var paint = Paint().apply { this.color = Color.RED }
            var canvas = Canvas(drawBitmap)
            canvas.drawBitmap(bitmap, 0f, 0f, null)

            _binding.imageView.invalidate()
            //pose detection process change bitmap reference so image change
            PoseDetectionProcess(
                poseDetector,
                canvas,
                paint,
                bitmap,
                _binding.imageView
            ).processPose {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }


        }


    }


    private fun startCamera() {
        //pose detection innitiliazed
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener(Runnable {
            //preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(_binding.viewFinder.surfaceProvider)
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


