package dev.cisnux.dietarytestxml.presentation.foodscanner

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.CameraController.IMAGE_CAPTURE
import androidx.camera.view.LifecycleCameraController
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.cisnux.dietarytestxml.databinding.FragmentFoodScannerBinding
import dev.cisnux.dietarytestxml.presentation.healthprofile.HealthProfileDialog
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FoodScannerFragment : Fragment() {
    private val viewModel: FoodScannerViewModel by viewModels()
    private var _binding: FragmentFoodScannerBinding? = null
    private val binding get() = _binding!!
    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                viewModel.fileFromUri(image = uri)
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFoodScannerBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val cameraController = LifecycleCameraController(requireContext())
        cameraController.isTapToFocusEnabled
        cameraController.setEnabledUseCases(IMAGE_CAPTURE)
        cameraController.bindToLifecycle(viewLifecycleOwner)
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.isBackCamera.collectLatest { isBackCamera ->
                    cameraController.cameraSelector =
                        if (isBackCamera) CameraSelector.DEFAULT_BACK_CAMERA
                        else CameraSelector.DEFAULT_FRONT_CAMERA
                }
            }
        }
        with(binding) {
            previewView.controller = cameraController
            backIconButton.setOnClickListener {
                findNavController().navigateUp()
            }
            takePictureBtn.setOnClickListener {
                HealthProfileDialog(
                    onUpdate = {
                        val navigateToUpdateHealthProfileFragment = FoodScannerFragmentDirections.actionFoodScannerFragmentToUpdateHealthProfileFragment()
                        findNavController().navigate(navigateToUpdateHealthProfileFragment)
                    },
                    onDone = { takePictureWithCamera() }).show(
                    requireActivity().supportFragmentManager,
                    HealthProfileDialog::class.simpleName
                )
            }
            galleryBtn.setOnClickListener {
                HealthProfileDialog(
                    onUpdate = {},
                    onDone = { takePictureFromGallery() }).show(
                    requireActivity().supportFragmentManager,
                    HealthProfileDialog::class.simpleName
                )
            }
            rotateBtn.setOnClickListener {
                viewModel.updateOnCameraChange(isBackCamera = !viewModel.isBackCamera.value)
            }
        }
        // from camera and gallery
        onCapturedByCamera(cameraController = cameraController)
        onCapturedByGallery()
    }

    private fun onCapturedByGallery() = viewLifecycleOwner.lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.RESUMED) {
            viewModel.galleryFile
                .drop(1)
                .collectLatest {
                    it?.let { file ->
                        val navigateToScannerResult =
                            FoodScannerFragmentDirections.actionFoodScannerFragmentToScannerResultFragment(
                                file.path,
                                false,
                                viewModel.isBackCamera.value
                            )
                        findNavController().navigate(navigateToScannerResult)
                    }
                }
        }
    }

    private fun onCapturedByCamera(cameraController: LifecycleCameraController) =
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.cameraFile
                    .drop(1)
                    .collectLatest {
                        it?.let { file ->
                            val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()
                            cameraController.takePicture(
                                outputOptions,
                                ContextCompat.getMainExecutor(requireActivity()),
                                object : ImageCapture.OnImageSavedCallback {
                                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                                        val navigateToScannerResult =
                                            FoodScannerFragmentDirections.actionFoodScannerFragmentToScannerResultFragment(
                                                file.path,
                                                true,
                                                viewModel.isBackCamera.value
                                            )
                                        findNavController().navigate(navigateToScannerResult)
                                    }

                                    override fun onError(exception: ImageCaptureException) {
                                        Toast.makeText(
                                            requireActivity(),
                                            "failed to took picture",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            )
                        }
                    }
            }
        }

    private fun takePictureFromGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun takePictureWithCamera() =
        viewModel.createFile()

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}