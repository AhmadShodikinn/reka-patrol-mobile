package com.project.rekapatrol.support

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

fun String.toPlainPart(): RequestBody =
    this.toRequestBody("text/plain".toMediaTypeOrNull())

fun Int.toPlainPart(): RequestBody {
    return this.toString().toRequestBody("text/plain".toMediaTypeOrNull())
}