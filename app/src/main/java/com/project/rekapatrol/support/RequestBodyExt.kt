package com.project.rekapatrol.support

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

fun String.toPlainPart(): RequestBody =
    this.toRequestBody("text/plain".toMediaTypeOrNull())

fun Int.toPlainPart(): RequestBody {
    return this.toString().toRequestBody("text/plain".toMediaTypeOrNull())
}

fun Boolean.toIntPlainPart(): RequestBody {
    val intValue = if (this) 1 else 0
    return RequestBody.create("text/plain".toMediaTypeOrNull(), intValue.toString())
}