package dev.cisnux.dietarytestxml.presentation.scannerresult

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.cisnux.dietarytestxml.data.locals.FileService
import javax.inject.Inject

@HiltViewModel
class ScannerResultViewModel @Inject constructor(
    private val fileService: FileService
) : ViewModel() {
    suspend fun rotateFile(filePath: String, isBackCamera: Boolean = false) =
        fileService.rotateFile(filePath = filePath, isBackCamera = isBackCamera)
}