package com.project.rekapatrol.ui.helper

import android.content.Context
import android.graphics.Bitmap
import java.io.File

fun Bitmap.compressToUnderSize(context: Context, maxSizeKB: Int = 2048): File {
    var quality = 90
    lateinit var file: File
    do {
        file = File(context.cacheDir, "compressed_${System.currentTimeMillis()}.jpg")
        file.outputStream().use { out ->
            this.compress(Bitmap.CompressFormat.JPEG, quality, out)
        }
        quality -= 10
    } while (file.length() / 1024 > maxSizeKB && quality > 10)
    return file
}