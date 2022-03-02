package com.oguzhancetin.goodpostureapp.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.*
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.core.impl.PreviewConfig
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.mlkit.vision.pose.PoseDetector
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions
import com.oguzhancetin.goodpostureapp.*
import com.oguzhancetin.goodpostureapp.databinding.FragmentExercisesBinding
import com.oguzhancetin.goodpostureapp.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_main.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject


@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>() {

    private val args: MainFragmentArgs by navArgs()

    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private var width:Int = 0
    private var height:Int = 0

    @Inject
    lateinit var poseDetector: PoseDetector

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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val globalLayoutListener = object : ViewTreeObserver.OnGlobalLayoutListener{
            override fun onGlobalLayout() {
                view.viewTreeObserver.removeOnGlobalLayoutListener(this);
                height = binding.imageviewCamera.measuredHeight
                width = binding.imageviewCamera.measuredWidth//1080 _binding.imageView.measuredWidth

                args.uri?.let {
                    val uri = Uri.parse(it)
                    setImageView(uri)

                    binding.imageviewCamera.measure(
                        View.MeasureSpec.UNSPECIFIED,
                        View.MeasureSpec.UNSPECIFIED
                    )

                }
            }

        }

        /**
        val fragmentResultDialog = ShowResultDialogFragment()
        fragmentResultDialog.show(childFragmentManager, fragmentResultDialog.tag)
         **/

        binding.imageviewCamera.viewTreeObserver. addOnGlobalLayoutListener(globalLayoutListener) //1397
        outputDirectory = getOutputDirectory(requireActivity().application)
        cameraExecutor = Executors.newSingleThreadExecutor()

        binding.cameraCaptureButton.setOnClickListener { takePhoto() }
        binding.btnClear.setOnClickListener { clearScreen() }
        binding.buttonFromDevice.setOnClickListener { goToGallery() }


        checkPermissionsOk()

    }

    private fun goToGallery() {
        val navController =  findNavController()
        val direction = MainFragmentDirections.actionMainFragmentToGalleryFragment3()
        navController.navigate(direction)
    }


    private fun checkPermissionsOk() {
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }
    }

    private fun clearScreen() {
        binding.apply {
            viewFinder.visibility = View.VISIBLE
            imageviewCamera.visibility = View.INVISIBLE
            btnClear.visibility = View.INVISIBLE
        }


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
                    requireActivity(),
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
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
            ContextCompat.getMainExecutor(requireActivity()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    //Uri.fromFile(photoFile)
                    val savedUri =
                        Uri.fromFile(photoFile) //Uri.parse("file:///storage/emulated/0/Android/media/com.oguzhancetin.goodpostureapp/GoodPostureApp/2021-11-26-12-01-36-293.jpg") //
                    val msg = "Photo capture succeeded: $savedUri"
                    setImageView(savedUri)
                    binding.btnClear.visibility = View.VISIBLE
                    //Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
                    Log.d(TAG, msg)
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exception.message}", exception)
                }
            }
        )
    }

    private fun setImageView(uri: Uri?) {
        if (binding.viewFinder.visibility == View.VISIBLE) {
            binding.viewFinder.visibility = View.GONE
            binding.imageviewCamera.visibility = View.VISIBLE


            //empty bitmap with given size
            val drawBitmap = Bitmap.createBitmap(
                width,
                height,
                Bitmap.Config.ARGB_8888
            )
            //
            var bitmap = getResizedBitmap(
                BitmapFactory.decodeFile(uri?.encodedPath),
                width,
                height
            )


            var paint = Paint().apply { this.color = Color.RED }
            var canvas = Canvas(drawBitmap)
            canvas.drawBitmap(bitmap, 0f, 0f, null)

            binding.imageviewCamera.invalidate()
            //pose detection process change bitmap reference so image change
            val poseProcess = PoseDetectionProcess(
                poseDetector,
                canvas,
                paint,
                bitmap,
                binding.imageviewCamera
            )
            poseProcess.processPose() { processResult ->
                when (processResult) {
                    is ProcessResult.ProcessSucces -> {
                        Glide.with(this).load(drawBitmap).into(binding.imageviewCamera)
                        binding.imageviewCamera.invalidate()
                    }
                    is ProcessResult.ProcessError -> {
                        Toast.makeText(
                            requireContext(),
                            (processResult as? ProcessResult.ProcessError)?.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    else -> {}
                }
            }


        }


    }

    private fun startCamera() {
        //pose detection innitiliazed
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            //preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
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
        }, ContextCompat.getMainExecutor(requireActivity()))
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireContext(),
            it
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun getViewBinding() = FragmentMainBinding.inflate(layoutInflater)
}