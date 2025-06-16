package com.project.rekapatrol.ui.screen

import CameraPreviewScreen
import android.app.DatePickerDialog
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.project.rekapatrol.R
import com.project.rekapatrol.data.viewModel.GeneralViewModel
import com.project.rekapatrol.data.viewModelFactory.GeneralViewModelFactory
import com.project.rekapatrol.ui.helper.FullscreenImageView
import com.project.rekapatrol.ui.helper.ImageDialogs
import com.project.rekapatrol.ui.helper.ImagePickerSection
import com.project.rekapatrol.ui.helper.uriToMultipartFinding
import com.project.rekapatrol.ui.theme.cream
import com.project.rekapatrol.ui.theme.skyblue
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputSafetyPatrolScreen(
    navController: NavController,
    safetyPatrolId: Int? = null,
    isTindakLanjut: Boolean = false,
    viewModel: GeneralViewModel = viewModel(factory = GeneralViewModelFactory(LocalContext.current))
) {
    val context = LocalContext.current
    val isEditMode = safetyPatrolId != null && !isTindakLanjut

    // Form states
    var temuan by remember { mutableStateOf("") }
    var lokasi by remember { mutableStateOf("") }
    var kategori by remember { mutableStateOf("") }
    var resiko by remember { mutableStateOf("") }
    var tanggal by remember { mutableStateOf("") }

    val kategoriOptions = listOf("Unsafe Condition", "Unsafe Action")

    val resikoOptions = listOf("Rendah", "Sedang", "Tinggi")
    var expandedKategori by remember { mutableStateOf(false) }
    var expandedResiko by remember { mutableStateOf(false) }

    // Image picker states
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var imageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }
    var isCameraActive by remember { mutableStateOf(false) }

    // Date picker
    val calendar = Calendar.getInstance()
    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                tanggal = "$year-${month + 1}-$dayOfMonth"
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    // Gallery launcher
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri> ->
        imageUris = uris
    }

    var showImageOptionsDialog by remember { mutableStateOf(false) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var showSourceDialog by remember { mutableStateOf(false) }
    var isImageFullscreen by remember { mutableStateOf(false) }

    var imageUrl by remember { mutableStateOf<String?>(null) }

    val inputResult by viewModel.inputSafetyPatrolsResponse.observeAsState()
    val updateResult by viewModel.updateSafetyPatrolResponse.observeAsState()

    val detail by viewModel.safetyPatrolDetailResponse.observeAsState()

    LaunchedEffect(safetyPatrolId) {
        if (safetyPatrolId != null) {
            viewModel.getDetailSafetyPatrol(safetyPatrolId)
        }
    }

    LaunchedEffect(detail) {
        detail?.data?.let {
            temuan = it.findingsDescription.toString()
            lokasi = it.location.toString()
            kategori = it.category.toString()
            resiko = it.risk.toString()
            tanggal = it.checkupDate.toString()
            val baseUrl = "http://103.211.26.90/storage/"
            imageUrl = it.findings?.get(0)?.imagePath?.let { path -> "$baseUrl$path" }
        }

        Log.d("InputsafetyPatrol", detail.toString())
    }


    LaunchedEffect(inputResult) {
        inputResult?.let {
            Toast.makeText(context, "Berhasil submit data!", Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        }
    }

    LaunchedEffect(updateResult) {
        updateResult?.let {
            Toast.makeText(context, "Berhasil update inspeksi!", Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            if (!isCameraActive) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            if (isTindakLanjut) "Tindak Lanjut Safety Patrol" else if (isEditMode) "Update Safety Patrol" else "Input Safety Patrol",
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
                    }
                )
            }
        },
        containerColor = Color.White
    ) { paddingValues ->
        if (isImageFullscreen && selectedImageUri != null) {
            //Showing image
            FullscreenImageView(
                imageUri = selectedImageUri!!,
                onClose = { isImageFullscreen = false }
            )
        }
        else if (isCameraActive) {
            // CameraX
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
                    .padding(16.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OutlinedTextField(
                    value = temuan,
                    onValueChange = { temuan = it },
                    label = { Text("Temuan") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = lokasi,
                    onValueChange = { lokasi = it },
                    label = { Text("Lokasi") },
                    modifier = Modifier.fillMaxWidth()
                )

                ImagePickerSection(
                    imageUris = imageUris,
                    imageUrl = imageUrl,
                    onImageClick = { uriOrNull ->
                        uriOrNull?.let {
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

                // Dropdown Kategori
                ExposedDropdownMenuBox(
                    expanded = expandedKategori,
                    onExpandedChange = { expandedKategori = !expandedKategori }
                ) {
                    OutlinedTextField(
                        value = kategori,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Kategori") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedKategori) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedKategori,
                        onDismissRequest = { expandedKategori = false }
                    ) {
                        kategoriOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    kategori = option
                                    expandedKategori = false
                                }
                            )
                        }
                    }
                }

                // Dropdown Resiko
                ExposedDropdownMenuBox(
                    expanded = expandedResiko,
                    onExpandedChange = { expandedResiko = !expandedResiko }
                ) {
                    OutlinedTextField(
                        value = resiko,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Resiko") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedResiko) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedResiko,
                        onDismissRequest = { expandedResiko = false }
                    ) {
                        resikoOptions.forEach {
                            DropdownMenuItem(
                                text = { Text(it) },
                                onClick = {
                                    resiko = it
                                    expandedResiko = false
                                }
                            )
                        }
                    }
                }

                // Date Picker
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            datePickerDialog.show()
                        }
                ) {
                    OutlinedTextField(
                        value = tanggal,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Tanggal Pemeriksaan") },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = false
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (isTindakLanjut) {
                            navController.navigate("tindakLanjutSafetyPatrol/${safetyPatrolId}")
                        } else {
                            if (
                                temuan.isNotBlank() && lokasi.isNotBlank() && kategori.isNotBlank() &&
                                resiko.isNotBlank() && (imageUris.isNotEmpty() || !imageUrl.isNullOrBlank()) && tanggal.isNotEmpty()
                            ) {
                                val multipartFiles = imageUris.map { uri ->
                                    uriToMultipartFinding(context, uri)
                                }

                                if (isEditMode) {
                                    viewModel.updateSafetyPatrol(
                                        safetyPatrolId = safetyPatrolId!!,
                                        findingPaths = multipartFiles,
                                        findingDescription = temuan,
                                        location = lokasi,
                                        category = kategori,
                                        risk = resiko,
                                        checkupDate = tanggal
                                    )
                                } else {
                                    viewModel.inputSafetyPatrol(
                                        findingPaths = multipartFiles,
                                        findingDescription = temuan,
                                        location = lokasi,
                                        category = kategori,
                                        risk = resiko,
                                        checkupDate = tanggal
                                    )
                                }
                            } else {
                                Toast.makeText(context, "Mohon lengkapi semua data", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = skyblue)
                ) {
                    Text(
                        when {
                            isTindakLanjut -> "Selanjutnya"
                            isEditMode -> "Update"
                            else -> "Submit"
                        },
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
fun InputSafetyScreenPreview() {
    InputSafetyPatrolScreen(navController = rememberNavController())
}
