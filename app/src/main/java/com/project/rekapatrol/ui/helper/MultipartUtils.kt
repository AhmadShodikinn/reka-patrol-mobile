package com.project.rekapatrol.ui.helper

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream

fun uriToMultipartFinding(context: Context, uri: Uri, maxSizeKB: Int = 512): MultipartBody.Part {
    val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
    val resizedBitmap = bitmap.resizeToMax(1024, 768)
    val compressedFile = resizedBitmap.compressToUnderSize(context, maxSizeKB)
    val requestBody = compressedFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
    return MultipartBody.Part.createFormData("finding_paths[]", compressedFile.name, requestBody)
}

fun uriToMultipartAction(context: Context, uri: Uri, maxSizeKB: Int = 512): MultipartBody.Part {
    val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
    val resizedBitmap = bitmap.resizeToMax(1024, 768)
    val compressedFile = resizedBitmap.compressToUnderSize(context, maxSizeKB)
    val requestBody = compressedFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
    return MultipartBody.Part.createFormData("action_path", compressedFile.name, requestBody)
}

fun createPdfMultipart(context: Context, uri: Uri): MultipartBody.Part? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri) ?: return null
        val file = File(context.cacheDir, "temp_upload.pdf")
        file.outputStream().use { inputStream.copyTo(it) }

        val requestBody = file
            .asRequestBody("application/pdf".toMediaTypeOrNull())
        MultipartBody.Part.createFormData("file", file.name, requestBody)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun createMemoIdPart(memoId: Int?): MultipartBody.Part {
    val value = memoId?.toString() ?: ""
    val requestBody = value.toRequestBody("text/plain".toMediaTypeOrNull())
    return MultipartBody.Part.createFormData("memo_id", null, requestBody)
}



