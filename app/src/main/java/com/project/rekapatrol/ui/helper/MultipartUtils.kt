package com.project.rekapatrol.ui.helper

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

fun uriToMultipartFinding(context: Context, uri: Uri, maxSizeKB: Int = 2048): MultipartBody.Part {
    val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
    val compressedFile = bitmap.compressToUnderSize(context, maxSizeKB)
    val requestBody = compressedFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
    return MultipartBody.Part.createFormData("finding_paths[]", compressedFile.name, requestBody)
}

fun uriToMultipartAction(context: Context, uri: Uri, maxSizeKB: Int = 2048): MultipartBody.Part {
    val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
    val compressedFile = bitmap.compressToUnderSize(context, maxSizeKB)
    val requestBody = compressedFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
    return MultipartBody.Part.createFormData("action_path", compressedFile.name, requestBody)
}



