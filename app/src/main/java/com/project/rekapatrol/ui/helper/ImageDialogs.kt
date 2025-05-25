package com.project.rekapatrol.ui.helper

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.project.rekapatrol.R

@Composable
fun ImageDialogs(
    selectedImageUri: Uri?,
    showImageOptionsDialog: Boolean,
    onDismissImageOptions: () -> Unit,
    onViewImage: () -> Unit,
    onChangeImage: () -> Unit,
    showSourceDialog: Boolean,
    onDismissSourceDialog: () -> Unit,
    onSelectCamera: () -> Unit,
    onSelectGallery: () -> Unit,
    showViewImageDialog: Boolean,
    onDismissViewImageDialog: () -> Unit
) {
    if (showImageOptionsDialog && selectedImageUri != null) {
        AlertDialog(
            onDismissRequest = onDismissImageOptions,
            title = { Text("Gambar") },
            text = { Text("Pilih aksi untuk gambar ini:") },
            confirmButton = {
                TextButton(onClick = {
                    onDismissImageOptions()
                    onViewImage()
                }) {
                    Text("Lihat Gambar")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    onDismissImageOptions()
                    onChangeImage()
                }) {
                    Text("Ubah Gambar")
                }
            }
        )
    }

    if (showSourceDialog) {
        AlertDialog(
            onDismissRequest = onDismissSourceDialog,
            title = { Text("Pilih sumber gambar") },
            confirmButton = {
                TextButton(onClick = {
                    onDismissSourceDialog()
                    onSelectCamera()
                }) {
                    Text("Kamera")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    onDismissSourceDialog()
                    onSelectGallery()
                }) {
                    Text("Galeri")
                }
            }
        )
    }

    if (showViewImageDialog && selectedImageUri != null) {
        FullscreenImageView(
            imageUri = selectedImageUri,
            onClose = onDismissViewImageDialog
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ImagePickerSection(
    imageUris: List<Uri>,
    imageUrl: String? = null,
    onImageClick: (Uri) -> Unit,
    onAddImageClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(Color(0xFFEFEFEF), shape = RoundedCornerShape(8.dp))
            .border(BorderStroke(1.dp, Color.Gray), shape = RoundedCornerShape(8.dp))
            .clickable {
                when {
                    imageUris.isNotEmpty() -> onImageClick(imageUris[0])
                    !imageUrl.isNullOrEmpty() -> onImageClick(Uri.parse(imageUrl))
                    else -> onAddImageClick()
                }
            },
        contentAlignment = Alignment.Center
    ) {
        when {
            imageUris.isNotEmpty() -> {
                GlideImage(
                    model = imageUris[0],
                    contentDescription = "Gambar terpilih",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp))
                )
            }
            !imageUrl.isNullOrEmpty() -> {
                GlideImage(
                    model = imageUrl,
                    contentDescription = "Gambar dari server",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp))
                )
            }
            else -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.imagesmode),
                        contentDescription = "Tambah Gambar",
                        modifier = Modifier.size(120.dp)
                    )
                    Text("Tambah Gambar", color = Color.Gray)
                }
            }
        }
    }
}
