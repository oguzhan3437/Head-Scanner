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
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.window.layout.WindowMetricsCalculator
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.mlkit.vision.pose.PoseDetector
import com.oguzhancetin.goodpostureapp.*
import com.oguzhancetin.goodpostureapp.data.model.Record
import com.oguzhancetin.goodpostureapp.databinding.FragmentMainBinding
import com.oguzhancetin.goodpostureapp.util.PoseDetectionProcess
import com.oguzhancetin.goodpostureapp.util.ProcessResult
import com.oguzhancetin.goodpostureapp.util.getResizedBitmap
import com.oguzhancetin.goodpostureapp.viewmodel.CameraViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject


@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>() {

    private val args: MainFragmentArgs by navArgs()
    private val viewModel: CameraViewModel by viewModels()

    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private var width: Int = 0
    private var height: Int = 0
    private var currentPhotoUri: Uri? = null

    @Inject
    lateinit var poseDetector: PoseDetector


    companion object {
        private const val TAG = "CameraXBasic"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 12
        private val REQUIRED_PERMISSIONS =
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermissionsOk()
        val globalLayoutListener = object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                height = binding.imageviewCamera.measuredHeight
                width = binding.imageviewCamera.measuredWidth//1080 _binding.imageView.measuredWidth
                binding.imageviewCamera.measure(
                    View.MeasureSpec.UNSPECIFIED,
                    View.MeasureSpec.UNSPECIFIED
                )
                args.apply {
                    uri?.let {
                        val uri = Uri.parse(it)
                        currentPhotoUri = uri
                        setImageView(uri)
                    }
                }
            }
        }
        binding.imageviewCamera.viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)
        outputDirectory = getOutputDirectory(requireActivity().application)
        cameraExecutor = Executors.newSingleThreadExecutor()

        binding.cameraCaptureButton.setOnClickListener { takePhoto() }
        binding.btnClear.setOnClickListener { clearScreen() }
        binding.buttonFromDevice.setOnClickListener { goToGallery() }

        showCameraAlertPopUp()
    }


    private fun goToGallery() {
        val navController = findNavController()
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
                    val savedUri =
                        Uri.fromFile(photoFile) //Uri.parse("file:///storage/emulated/0/Android/media/com.oguzhancetin.goodpostureapp/GoodPostureApp/2021-11-26-12-01-36-293.jpg") //
                    currentPhotoUri = savedUri
                    val msg = "Photo capture succeeded: $savedUri"
                    setImageView(savedUri)
                    binding.btnClear.visibility = View.VISIBLE
                    binding.buttonFromDevice.visibility = View.INVISIBLE
                    binding.cameraCaptureButton.visibility = View.INVISIBLE
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
            binding.viewFinder.visibility = View.INVISIBLE
            binding.imageviewCamera.visibility = View.VISIBLE

            binding.btnClear.visibility = View.VISIBLE
            binding.buttonFromDevice.visibility = View.INVISIBLE
            binding.cameraCaptureButton.visibility = View.INVISIBLE

            //empty bitmap with given size
            val drawBitmap = Bitmap.createBitmap(
                width,
                height,
                Bitmap.Config.ARGB_8888
            )
            //resize and covert saved photo
            val bitmap = getResizedBitmap(
                BitmapFactory.decodeFile(uri?.encodedPath),
                width,
                height
            )
            //created canvas empty bitmap(fixed size) and put saved photo bitmap onto empty
            val canvas = Canvas(drawBitmap)
            canvas.drawBitmap(bitmap, 0f, 0f, null)
            val paint1 = Paint().apply {
                color = ContextCompat.getColor(
                    this@MainFragment.requireActivity(),
                    R.color.scanner_color_1
                )
                this.strokeWidth = 2f
            }
            val paint2 = Paint().apply {
                color = ContextCompat.getColor(
                    this@MainFragment.requireActivity(),
                    R.color.scanner_color_2
                )
                this.strokeWidth = 2f
                this.pathEffect =
                    setPathEffect(DashPathEffect(floatArrayOf(5f, 10f, 15f, 20f, 0f), 0f))
            }

            Glide.with(this@MainFragment).load(drawBitmap).into(binding.imageviewCamera)
            binding.imageviewCamera.invalidate()

            binding.determinateBar.visibility = View.VISIBLE
            this.viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                Log.e("deneme", "3213123")
                delay(2000)
                startImageProcessing(poseDetector, canvas, paint1, paint2, bitmap, drawBitmap)
            }
        }
    }

    /**
     * canvas created with empty bitmap
     * drawBitmap is saved photo
     */
    private fun startImageProcessing(
        poseDetector: PoseDetector,
        canvas: Canvas,
        paint1: Paint,
        paint2: Paint,
        bitmap: Bitmap,
        drawBitmap: Bitmap
    ) {
        val poseProcess = PoseDetectionProcess(
            poseDetector,
            canvas,
            paint1,
            paint2,
            bitmap,
        )
        poseProcess.processPose { processResult ->
            when (processResult) {
                is ProcessResult.ProcessSuccess -> {

                    Glide.with(binding.imageviewCamera).load(drawBitmap)
                        .into(binding.imageviewCamera)
                    binding.imageviewCamera.invalidate()
                    processResult.degree?.let { degree ->
                        showResultBottomSheet(degree.toInt())
                    }
                    binding.determinateBar.visibility = View.INVISIBLE
                }
                is ProcessResult.ProcessError -> {
                    Toast.makeText(
                        requireContext(),
                        (processResult as? ProcessResult.ProcessError)?.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.determinateBar.visibility = View.INVISIBLE
                }

                else -> {
                    binding.determinateBar.visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun clearScreen() {
        binding.apply {
            viewFinder.visibility = View.VISIBLE
            imageviewCamera.visibility = View.INVISIBLE
            btnClear.visibility = View.INVISIBLE
            buttonFromDevice.visibility = View.VISIBLE
            cameraCaptureButton.visibility = View.VISIBLE



        }
    }

    private fun startCamera() {
        val metrics = WindowMetricsCalculator.getOrCreate()
            .computeCurrentWindowMetrics(this.requireActivity()).bounds
        val screenAspectRatio = aspectRatio(metrics.width(), metrics.height())
        //pose detection innitiliazed
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            //preview
            val preview = Preview.Builder()
                .setTargetAspectRatio(screenAspectRatio)
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            imageCapture = ImageCapture.Builder()
                .setTargetAspectRatio(screenAspectRatio)
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

    private fun showResultBottomSheet(degree: Int) {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd",Locale.US)
        val currentDateAndTime: String = simpleDateFormat.format(Date())
        if (args.isRecordedPhoto.not()) {
            val bottomSheet = ResultBottomSheet(degree) {
                viewModel.insert(
                    Record(
                        title = currentDateAndTime,
                        imageUri = currentPhotoUri.toString(),
                        id = null
                    )
                )
            }
            bottomSheet.show(parentFragmentManager, "BottomSheet")
        }
    }

    private fun showCameraAlertPopUp() {
        viewModel.showCameraAlerStatus.observe(this.viewLifecycleOwner) {
            if (it) {
                MaterialAlertDialogBuilder(
                    this.requireContext(),
                    R.style.ThemeOverlay_MaterialComponents_Dialog_Alert
                )
                    .setMessage("You must take picture left side")
                    .setNegativeButton("Ok") { dialog, which ->

                    }
                    .setPositiveButton("Never Ask Again") { dialog, which ->
                        viewModel.changeShowCameraAlertStatus(false)
                    }
                    .show()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireContext(),
            it
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cameraExecutor.shutdown()
    }

    override fun getViewBinding() = FragmentMainBinding.inflate(layoutInflater)
}
