package com.project.rekapatrol.ui.helper

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.RequiresApi
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

@RequiresApi(Build.VERSION_CODES.Q)
fun savePdfToDownloads(context: Context, body: ResponseBody, fileName: String) {
    try {
        val resolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.Downloads.DISPLAY_NAME, fileName)
            put(MediaStore.Downloads.MIME_TYPE, "application/pdf")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                put(MediaStore.Downloads.IS_PENDING, 1)
            }
        }

        val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

        if (uri != null) {
            val outputStream: OutputStream? = resolver.openOutputStream(uri)
            outputStream.use { output ->
                body.byteStream().use { input ->
                    input.copyTo(output!!)
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                contentValues.clear()
                contentValues.put(MediaStore.Downloads.IS_PENDING, 0)
                resolver.update(uri, contentValues, null, null)
            }

            Toast.makeText(context, "File disimpan di Downloads: $fileName", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(context, "Gagal menyimpan file", Toast.LENGTH_SHORT).show()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Terjadi kesalahan: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
    }
}

fun savePdftoDownloadUnder(context: Context, body: ResponseBody, fileName: String) {
    try {
        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        if (!downloadsDir.exists()) downloadsDir.mkdirs()

        val file = File(downloadsDir, fileName)
        val outputStream = FileOutputStream(file)

        body.byteStream().use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }

        Toast.makeText(context, "File disimpan di: ${file.absolutePath}", Toast.LENGTH_LONG).show()
    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Gagal simpan file: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
    }
}