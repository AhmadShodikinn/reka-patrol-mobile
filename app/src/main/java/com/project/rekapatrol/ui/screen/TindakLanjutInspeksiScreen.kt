package com.project.rekapatrol.ui.screen

import CameraPreviewScreen
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.project.rekapatrol.R
import com.project.rekapatrol.data.viewModel.GeneralViewModel
import com.project.rekapatrol.data.viewModelFactory.GeneralViewModelFactory
import com.project.rekapatrol.ui.helper.FullscreenImageView
import com.project.rekapatrol.ui.helper.ImageDialogs
import com.project.rekapatrol.ui.helper.ImagePickerSection
import com.project.rekapatrol.ui.helper.uriToMultipartAction
import com.project.rekapatrol.ui.theme.cream
import com.project.rekapatrol.ui.theme.skyblue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TindakLanjutInspeksiScreen(navController: NavController, inspectionId: Int) {
    val context = LocalContext.current
    val generalViewModel: GeneralViewModel = viewModel(factory = GeneralViewModelFactory(context))

    // Memantau data inspeksi yang diterima
    val inspectionDetail by generalViewModel.inspectionDetailResposne.observeAsState()

    // Menyediakan fallback value jika data masih kosong
//    val criteriaName = inspectionDetail?.data?.criteria?.criteriaName ?: "Memuat kriteria..."
    val findingDescription = inspectionDetail?.data?.findingsDescription ?: "Memuat Deskripsi Temuan..."

    // State untuk input dan gambar
    var tindakLanjut by remember { mutableStateOf("") }

    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var imageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }
    var isCameraActive by remember { mutableStateOf(false) }

    val result by generalViewModel.updateInspectionActionResponse.observeAsState()

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        imageUris = uris
    }

    var showImageOptionsDialog by remember { mutableStateOf(false) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var showSourceDialog by remember { mutableStateOf(false) }
    var isImageFullscreen by remember { mutableStateOf(false) }

    var imageUrl by remember { mutableStateOf<String?>(null) }

    // Pastikan data inspeksi diambil saat komponen dimuat
    LaunchedEffect(inspectionId) {
        Log.d("TindakLanjutInspeksi", "panggil detailInspeksi")
        generalViewModel.getDetailInspection(inspectionId)
    }

    LaunchedEffect(inspectionDetail) {
        inspectionDetail?.let {
            // Data sudah diterima, lakukan sesuatu jika perlu
            Log.d("API_RESPONSE", "Criteria Name: ${it.data?.criteria?.criteriaName}")
        }
    }

    // Tangani hasil submit
    LaunchedEffect(result) {
        result?.let {
            Toast.makeText(context, "Berhasil submit data inspeksi!", Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        }
    }

    val criteriaType = inspectionDetail?.data?.criteria?.criteriaType ?: ""

    Scaffold(
        topBar = {
            if (!isCameraActive) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "Tindak Lanjut Inspeksi $criteriaType",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = skyblue,
                        navigationIconContentColor = Color.White
                    ),
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                        }
                    }
                )
            }
        },
        containerColor = Color.White
    ) { paddingValues ->
         if (isImageFullscreen && selectedImageUri != null) {
             FullscreenImageView(
                 imageUri = selectedImageUri!!,
                 onClose = { isImageFullscreen = false }
             )
         }
         else if (isCameraActive) {
            CameraPreviewScreen(
                onImageCaptured = { uri ->
                    imageUris = listOf(uri)
                    bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                    isCameraActive = false
                },
                onError = {
                    Toast.makeText(context, "Gagal ambil gambar", Toast.LENGTH_SHORT).show()
                    isCameraActive = false
                }
            )
        } else {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                Text(
                    text = "Deskripsi Temuan:",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = findingDescription,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(2.dp))

                ImagePickerSection(
                    imageUris = imageUris,
                    imageUrl = imageUrl,
                    onImageClick = { uriOrNull ->
                        uriOrNull.let {
                            selectedImageUri = it
                            showImageOptionsDialog = true
                        }
                    },
                    onAddImageClick = {
                        if (imageUris.isEmpty()) {
                            showSourceDialog = true
                        } else {
                            showImageOptionsDialog = true
                            selectedImageUri = imageUris.firstOrNull()
                        }
                    }
                )

                ImageDialogs(
                    selectedImageUri = selectedImageUri,
                    showImageOptionsDialog = showImageOptionsDialog,
                    onDismissImageOptions = { showImageOptionsDialog = false },
                    onViewImage = { isImageFullscreen = true },
                    onChangeImage = { showSourceDialog = true },
                    showSourceDialog = showSourceDialog,
                    onDismissSourceDialog = { showSourceDialog = false },
                    onSelectCamera = { isCameraActive = true },
                    onSelectGallery = { galleryLauncher.launch("image/*") },
                    showViewImageDialog = isImageFullscreen,
                    onDismissViewImageDialog = { isImageFullscreen = false }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Tindak Lanjut
                Text("Tindak Lanjut:", fontWeight = FontWeight.Medium)
                OutlinedTextField(
                    value = tindakLanjut,
                    onValueChange = { tindakLanjut = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    placeholder = { Text("Masukkan tindakan perbaikan...") },
                    maxLines = Int.MAX_VALUE
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (tindakLanjut.isNotBlank() && imageUris.isNotEmpty()) {
                            val imagePart = uriToMultipartAction(context, imageUris.first())
                            generalViewModel.TindakLanjutInspection(
                                inspectionId = inspectionId,
                                actionDescription = tindakLanjut,
                                actionImagePath = imagePart
                            )
                        } else {
                            Toast.makeText(context, "Mohon lengkapi semua data", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = skyblue,
                        contentColor = Color.White
                    )
                ) {
                    Text("Submit", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun TindakLanjutInspeksiScreenPreview() {
    TindakLanjutInspeksiScreen(navController = rememberNavController(), 1)
}
