package com.project.rekapatrol.ui.helper

import android.content.Context
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

fun uriToMultipartFinding(context: Context, uri: Uri): MultipartBody.Part {
    val file = File(context.cacheDir, "image.jpg")
    val inputStream = context.contentResolver.openInputStream(uri)
    val outputStream = FileOutputStream(file)
    inputStream?.copyTo(outputStream)
    inputStream?.close()
    outputStream.close()

    val requestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
    return MultipartBody.Part.createFormData("finding_paths[]", file.name, requestBody)
}

fun uriToMultipartAction(context: Context, uri: Uri): MultipartBody.Part {
    val file = File(context.cacheDir, "image.jpg")
    val inputStream = context.contentResolver.openInputStream(uri)
    val outputStream = FileOutputStream(file)
    inputStream?.copyTo(outputStream)
    inputStream?.close()
    outputStream.close()

    val requestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
    return MultipartBody.Part.createFormData("action_path", file.name, requestBody)
}



