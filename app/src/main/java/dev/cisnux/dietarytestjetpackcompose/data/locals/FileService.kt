package dev.cisnux.dietarytestjetpackcompose.data.locals

import android.net.Uri
import java.io.File

interface FileService {
    suspend fun createFile(): File
    suspend fun fileFromUri(image: Uri): File
}