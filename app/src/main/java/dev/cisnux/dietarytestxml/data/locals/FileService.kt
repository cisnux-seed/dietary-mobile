package dev.cisnux.dietarytestxml.data.locals

import android.net.Uri
import java.io.File

interface FileService {
    suspend fun createFile(): File
    suspend fun rotateFile(filePath: String, isBackCamera: Boolean = false)
    suspend fun fileFromUri(image: Uri): File
}