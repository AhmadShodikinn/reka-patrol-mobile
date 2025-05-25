package com.project.rekapatrol.ui.screen

import CameraPreviewScreen
import android.widget.Toast
import android.net.Uri
import android.provider.MediaStore
import android.graphics.Bitmap
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.project.rekapatrol.R
import com.project.rekapatrol.ui.theme.cream
import com.project.rekapatrol.ui.theme.skyblue
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.input.ImeAction
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.project.rekapatrol.data.viewModel.GeneralViewModel
import com.project.rekapatrol.data.viewModelFactory.GeneralViewModelFactory
import com.project.rekapatrol.ui.helper.FullscreenImageView
import com.project.rekapatrol.ui.helper.ImageDialogs
import com.project.rekapatrol.ui.helper.ImagePickerSection
import com.project.rekapatrol.ui.helper.uriToMultipartAction
import com.project.rekapatrol.ui.helper.uriToMultipartFinding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TindakLanjutSafetyPatrolScreen(navController: NavController, safetyPatrolId: Int) {
    val context = LocalContext.current
    val generalViewModel: GeneralViewModel = viewModel(factory = GeneralViewModelFactory(context))

    //form states
    var tindaklanjut by remember { mutableStateOf("") }

    // Image picker states
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var imageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }
    var isCameraActive by remember { mutableStateOf(false) }

    var showImageOptionsDialog by remember { mutableStateOf(false) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var showSourceDialog by remember { mutableStateOf(false) }
    var isImageFullscreen by remember { mutableStateOf(false) }

    val imageUrl by remember { mutableStateOf<String?>(null) }

    // Gallery launcher
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri>
        -> imageUris = uris
    }

    val result by generalViewModel.updateSafetyPatrolsActionResponse.observeAsState()

    // Displaying Toast when the screen is composed
    LaunchedEffect(result) {
        result?.let {
            Toast.makeText(context, "Berhasil submit data!", Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            if (!isCameraActive) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "Tindak Lanjut Safety Patrol",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = cream,
                        titleContentColor = Color.Black,
                        navigationIconContentColor = Color.White
                    ),
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                        }
                    },
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
            // Tampilkan layar kamera
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
            // Main Form UI
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
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
                Text(
                    text = "Tindak Lanjut:",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )

                OutlinedTextField(
                    value = tindaklanjut,
                    onValueChange = { tindaklanjut = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    label = { Text("Tindak Lanjut") },
                    placeholder = { Text("Masukkan Tindak Lanjut...") },
                    shape = RoundedCornerShape(8.dp),
                    maxLines = Int.MAX_VALUE
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (tindaklanjut.isNotBlank() && imageUris.isNotEmpty()) {
                            val multipartFile = uriToMultipartAction(context, imageUris.first())

                            generalViewModel.TindakLanjutSafetyPatrol(
                                safetyPatrolId = safetyPatrolId,
                                actionDescription = tindaklanjut,
                                actionImagePath = multipartFile
                            )
                        } else {
                            Toast.makeText(context, "Mohon lengkapi semua data", Toast.LENGTH_SHORT)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = cream,
                        contentColor = Color.Black
                    )
                ) {
                    Text(
                        text = "Submit",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun TindakLanjutSafetyPatrolScreenPreview() {
    TindakLanjutSafetyPatrolScreen(navController = rememberNavController(), safetyPatrolId = 1)
}
