package dev.cisnux.dietarytestxml.presentation.scannerresult

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.cisnux.dietarytestxml.databinding.FragmentScannerResultBinding
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ScannerResultFragment : Fragment() {
    private var _binding: FragmentScannerResultBinding? = null
    private val binding get() = _binding!!
    private var foodPicture: String? = null
    private var isRotatable: Boolean = false
    private var isBackCamera: Boolean = false
    private val viewModel: ScannerResultViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        foodPicture = ScannerResultFragmentArgs.fromBundle(arguments as Bundle).filePath
        isRotatable = ScannerResultFragmentArgs.fromBundle(arguments as Bundle).isRotatable
        isBackCamera = ScannerResultFragmentArgs.fromBundle(arguments as Bundle).isBackCamera
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScannerResultBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        foodPicture?.let {
            if (isRotatable) {
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.rotateFile(filePath = it, isBackCamera = isBackCamera)
                    binding.foodView.setImageBitmap(BitmapFactory.decodeFile(it))
                }
            } else {
                binding.foodView.setImageBitmap(BitmapFactory.decodeFile(it))
            }
        }
        binding.backIconButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}