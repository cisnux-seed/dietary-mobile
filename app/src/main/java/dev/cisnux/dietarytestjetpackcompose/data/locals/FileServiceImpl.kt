package dev.cisnux.dietarytestjetpackcompose.data.locals

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Environment
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import javax.inject.Inject

class FileServiceImpl @Inject constructor(
    @ApplicationContext private val application: Context,
) : FileService {
    override suspend fun createFile(): File = withContext(Dispatchers.IO) {
        val storageDir: File? = application.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        File.createTempFile(System.currentTimeMillis().toString(), ".jpg", storageDir)
    }

    override suspend fun rotateFile(filePath: String, isBackCamera: Boolean): Unit =
        withContext(Dispatchers.IO) {
            val matrix = Matrix()
            val bitmap = BitmapFactory.decodeFile(filePath)
            val rotation = if (isBackCamera) 90f else -90f
            matrix.postRotate(rotation)
            if (!isBackCamera) {
                matrix.postScale(-1f, 1f, bitmap.width / 2f, bitmap.height / 2f)
            }
            val result = Bitmap.createBitmap(
                bitmap,
                0,
                0,
                bitmap.width,
                bitmap.height,
                matrix,
                true
            )
            result.compress(
                Bitmap.CompressFormat.JPEG,
                100,
                FileOutputStream(filePath)
            )
        }

    override suspend fun fileFromUri(image: Uri): File = withContext(Dispatchers.IO) {
        val contentResolver = application.contentResolver
        val storageDir: File? = application.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val file = File.createTempFile(System.currentTimeMillis().toString(), ".jpg", storageDir)

        val inputStream = contentResolver.openInputStream(image) as InputStream
        val outputStream = FileOutputStream(file)
        val buf = ByteArray(1024)
        var len: Int
        while (inputStream.read(buf).also {
                len = it
            } > 0) outputStream.write(buf, 0, len)
        outputStream.close()
        inputStream.close()

        file
    }
}