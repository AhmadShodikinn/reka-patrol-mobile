package com.project.rekapatrol.ui.helper

import android.content.Context
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

fun uriToMultipart(context: Context, uri: Uri, partName: String = "finding_paths"): MultipartBody.Part {
    val inputStream = context.contentResolver.openInputStream(uri)
    val tempFile = File.createTempFile("upload_", ".jpg", context.cacheDir)
    inputStream?.use { input ->
        tempFile.outputStream().use { output ->
            input.copyTo(output)
        }
    }
    val requestBody = tempFile.asRequestBody("image/*".toMediaType())
    return MultipartBody.Part.createFormData(partName, tempFile.name, requestBody)
}
