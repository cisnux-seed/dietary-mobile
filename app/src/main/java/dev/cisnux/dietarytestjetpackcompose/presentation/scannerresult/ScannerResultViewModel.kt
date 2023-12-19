package dev.cisnux.dietarytestjetpackcompose.presentation.scannerresult

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.cisnux.dietarytestjetpackcompose.data.locals.FileService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScannerResultViewModel @Inject constructor(
    private val fileService: FileService,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private var _foodPicture =
        MutableStateFlow(checkNotNull(value = savedStateHandle["foodPicture"]) as String)
    val foodPicture get() = _foodPicture.asStateFlow()
    private var _isBackCamera =
        MutableStateFlow(checkNotNull(value = savedStateHandle["isBackCamera"]) as Boolean?)
    val isBackCamera get() = _isBackCamera.asStateFlow()

    fun rotateFile(filePath: String, isBackCamera: Boolean = false) = viewModelScope.launch {
        fileService.rotateFile(filePath = filePath, isBackCamera = isBackCamera)
    }
}