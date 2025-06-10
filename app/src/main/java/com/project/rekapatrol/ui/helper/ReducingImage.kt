package com.project.rekapatrol.ui.helper

import android.content.Context
import android.graphics.Bitmap
import java.io.File

fun Bitmap.resizeToMax(maxWidth: Int, maxHeight: Int): Bitmap {
    val width = this.width
    val height = this.height

    if (width <= maxWidth && height <= maxHeight) return this

    val aspectRatio = width.toFloat() / height.toFloat()
    val (newWidth, newHeight) = if (aspectRatio > 1) {
        maxWidth to (maxWidth / aspectRatio).toInt()
    } else {
        (maxHeight * aspectRatio).toInt() to maxHeight
    }

    return Bitmap.createScaledBitmap(this, newWidth, newHeight, true)
}

fun Bitmap.compressToUnderSize(context: Context, maxSizeKB: Int = 512): File {
    var quality = 90
    lateinit var file: File
    do {
        file = File(context.cacheDir, "compressed_${System.currentTimeMillis()}.jpg")
        file.outputStream().use { out ->
            this.compress(Bitmap.CompressFormat.JPEG, quality, out)
        }
        quality -= 5
    } while (file.length() / 1024 > maxSizeKB && quality > 10)
    return file
}