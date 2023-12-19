package dev.cisnux.dietarytestjetpackcompose.presentation.foodscanner

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.cisnux.dietarytestjetpackcompose.data.locals.FileService
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

    fun createFile() = viewModelScope.launch {
        _cameraFile.value = fileService.createFile()
    }

    fun fileFromUri(image: Uri) = viewModelScope.launch {
        _galleryFile.value = fileService.fileFromUri(image = image)
    }

    fun clearFile(){
        _cameraFile.value = null
        _galleryFile.value = null
    }
}