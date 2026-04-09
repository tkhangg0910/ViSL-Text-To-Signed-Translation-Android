package com.example.final_project.data.local.storage

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VideoStorage @Inject constructor(
    @param:ApplicationContext private val context: Context,
) {
    private val videosDir: File
        get() = File(context.filesDir, "videos").also { dir ->
            if (!dir.exists()) dir.mkdirs()
        }

    // Save
    suspend fun saveVideo(bytes: ByteArray): String? = withContext(Dispatchers.IO) {
        try {
            val fileName = "${UUID.randomUUID()}.mp4"
            val file = File(videosDir, fileName)
            file.writeBytes(bytes)
            file.absolutePath
        } catch (e: IOException) {
                android.util.Log.e("VideoStorage", "Failed to save video", e)
            null
        }
    }

    suspend fun saveVideoToDownloads(bytes: ByteArray): String? =
        withContext(Dispatchers.IO) {
            try {
                val fileName = "${UUID.randomUUID()}.mp4"

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val values = ContentValues().apply {
                        put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                        put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
                        put(MediaStore.MediaColumns.RELATIVE_PATH, "Download/ViSLVideos")
                    }

                    val uri = context.contentResolver.insert(
                        MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                        values
                    )

                    uri?.let {
                        context.contentResolver.openOutputStream(it)?.use { stream ->
                            stream.write(bytes)
                        }
                        return@withContext it.toString()
                    }

                    null
                } else {
                    val dir = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS
                    )

                    val folder = File(dir, "ViSLVideos")
                    if (!folder.exists()) folder.mkdirs()

                    val file = File(folder, fileName)
                    file.writeBytes(bytes)

                    file.absolutePath
                }

            } catch (e: Exception) {
                android.util.Log.e("VideoStorage", "Failed to save video", e)
                null
            }
        }

    // Read
    fun getVideoUri(path: String): Uri? {
        val file = File(path)
        return if (file.exists()) file.toUri() else null
    }

    // Delete
    suspend fun deleteVideo(path: String): Boolean = withContext(Dispatchers.IO) {
        val file = File(path)
        if (!file.exists()) return@withContext true   // đã xóa rồi → coi như thành công
        file.delete()
    }
    suspend fun deleteAll(): Int = withContext(Dispatchers.IO) {
        videosDir.listFiles()
            ?.count { it.isFile && it.extension == "mp4" && it.delete() }
            ?: 0
    }

    // Utility
    suspend fun totalSizeBytes(): Long = withContext(Dispatchers.IO) {
        videosDir.listFiles()
            ?.filter { it.isFile && it.extension == "mp4" }
            ?.sumOf { it.length() }
            ?: 0L
    }

}