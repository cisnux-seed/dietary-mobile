package dev.cisnux.dietarytestxml.presentation.foodscanner

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.cisnux.dietarytestxml.data.locals.FileService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class FoodScannerViewModel @Inject constructor(
    private val fileService: FileService
) : ViewModel() {
    private var _cameraFile: MutableStateFlow<File?> = MutableStateFlow(null)
    val cameraFile get() = _cameraFile.asStateFlow()
    private var _galleryFile: MutableStateFlow<File?> = MutableStateFlow(null)
    val galleryFile get() = _galleryFile.asStateFlow()
    private var _isBackCamera = MutableStateFlow(true)
    val isBackCamera get() = _isBackCamera.asStateFlow()

    fun createFile() = viewModelScope.launch {
        _cameraFile.value = fileService.createFile()
    }

    fun updateOnCameraChange(isBackCamera: Boolean) {
        _isBackCamera.value = isBackCamera
    }

    fun fileFromUri(image: Uri) = viewModelScope.launch {
        _galleryFile.value = fileService.fileFromUri(image = image)
    }
}