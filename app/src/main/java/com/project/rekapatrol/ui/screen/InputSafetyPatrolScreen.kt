package com.project.rekapatrol.ui.screen

import CameraPreviewScreen
import android.app.DatePickerDialog
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
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
import com.project.rekapatrol.R
import com.project.rekapatrol.data.viewModel.GeneralViewModel
import com.project.rekapatrol.data.viewModelFactory.GeneralViewModelFactory
import com.project.rekapatrol.ui.helper.uriToMultipartAction
import com.project.rekapatrol.ui.helper.uriToMultipartFinding
import com.project.rekapatrol.ui.theme.cream
import com.project.rekapatrol.ui.theme.skyblue
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputSafetyPatrolScreen(navController: NavController) {
    val context = LocalContext.current
    val generalViewModel: GeneralViewModel = viewModel(factory = GeneralViewModelFactory(context))

    // Form states
    var temuan by remember { mutableStateOf("") }
    var lokasi by remember { mutableStateOf("") }
    var kategori by remember { mutableStateOf("") }
    var resiko by remember { mutableStateOf("") }
    var tanggal by remember { mutableStateOf("") }

    val kategoriOptions = listOf("UC", "CA")
    val resikoOptions = listOf("Rendah", "Sedang", "Tinggi")
    var expandedKategori by remember { mutableStateOf(false) }
    var expandedResiko by remember { mutableStateOf(false) }

    // Image picker states
    var showDialog by remember { mutableStateOf(false) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var imageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }

    // Camera preview state
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

    //uji gambar
    var showImageOptionsDialog by remember { mutableStateOf(false) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val result by generalViewModel.inputSafetyPatrolsResponse.observeAsState()

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
                            "Input Safety Patrol",
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
        if (isCameraActive) {
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

//                ImagePickerSectionForInputSafetyPatrol(
//                    imageUris = imageUris,
//                    onClick = { showDialog = true }
//                )

                //uji gambar ada lihat
                ImagePickerSectionForInputSafetyPatrol(
                    imageUris = imageUris,
                    onImageClick = { uri ->
                        selectedImageUri = uri
                        showImageOptionsDialog = true
                    },
                    onAddImageClick = {
                        showDialog = true
                    }
                )

                if (showDialog) {
//                    AlertDialog(
//                        onDismissRequest = { showDialog = false },
//                        title = { Text("Pilih Gambar") },
//                        text = { Text("Pilih sumber gambar:") },
//                        confirmButton = {
//                            TextButton(onClick = {
//                                showDialog = false
//                                isCameraActive = true
//                            }) {
//                                Text("Kamera")
//                            }
//                        },
//                        dismissButton = {
//                            TextButton(onClick = {
//                                showDialog = false
//                                galleryLauncher.launch("image/*")
//                            }) {
//                                Text("Galeri")
//                            }
//                        }
//                    )

                    //uji gambar
                    if (showImageOptionsDialog && selectedImageUri != null) {
                        AlertDialog(
                            onDismissRequest = { showImageOptionsDialog = false },
                            title = { Text("Gambar") },
                            text = {
                                Column {
                                    Text("Pilih aksi untuk gambar ini:")
                                }
                            },
                            confirmButton = {
                                TextButton(onClick = {
                                    showImageOptionsDialog = false
                                    Toast.makeText(context, "Lihat gambar belum diimplementasikan", Toast.LENGTH_SHORT).show()
                                }) {
                                    Text("Lihat Gambar")
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = {
                                    showImageOptionsDialog = false
                                    showDialog = true // Ganti gambar
                                }) {
                                    Text("Ganti Gambar")
                                }
                            }
                        )
                    }
                }

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
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedKategori,
                        onDismissRequest = { expandedKategori = false }
                    ) {
                        kategoriOptions.forEach {
                            DropdownMenuItem(
                                text = { Text(it) },
                                onClick = {
                                    kategori = it
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
                        modifier = Modifier.menuAnchor().fillMaxWidth()
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
                        if (temuan.isNotBlank() && lokasi.isNotBlank() && kategori.isNotBlank() &&
                            resiko.isNotBlank() && imageUris.isNotEmpty() && tanggal.isNotEmpty()
                        ) {
                            val multipartFiles = imageUris.map { uri ->
                                uriToMultipartFinding(context, uri)
                            }

                            generalViewModel.inputSafetyPatrol(
                                findingPaths = multipartFiles,
                                findingDescription = temuan,
                                location = lokasi,
                                category = kategori,
                                risk = resiko,
                                checkupDate = tanggal
                            )
                        } else {
                            Toast.makeText(context, "Mohon lengkapi semua data", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = skyblue)
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

//@Composable
//fun ImagePickerSectionForInputSafetyPatrol(imageUris: List<Uri>, onClick: () -> Unit) {
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(200.dp)
//            .background(Color(0xFFEFEFEF), shape = RoundedCornerShape(8.dp))
//            .border(BorderStroke(1.dp, Color.Gray), shape = RoundedCornerShape(8.dp))
//            .clickable(onClick = onClick),
//        contentAlignment = Alignment.Center
//    ) {
//        if (imageUris.isNotEmpty()) {
//            LazyRow {
//                items(imageUris) { uri ->
//                    val bitmap = MediaStore.Images.Media.getBitmap(LocalContext.current.contentResolver, uri)
//                    Image(
//                        bitmap = bitmap.asImageBitmap(),
//                        contentDescription = "Gambar terpilih",
//                        modifier = Modifier
//                            .fillMaxHeight()
//                            .fillMaxWidth()
//                            .aspectRatio(16 / 9f, matchHeightConstraintsFirst = true) // Menjaga rasio
//                    )
//                }
//            }
//        } else {
//            Image(
//                painter = painterResource(id = R.drawable.imagesmode),
//                contentDescription = "Placeholder Gambar",
//                modifier = Modifier
//                    .size(120.dp)
//                    .align(Alignment.Center)
//            )
//        }
//    }
//}

//uji gambar ada lihat
@Composable
fun ImagePickerSectionForInputSafetyPatrol(
    imageUris: List<Uri>,
    onImageClick: (Uri) -> Unit,
    onAddImageClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(Color(0xFFEFEFEF), shape = RoundedCornerShape(8.dp))
            .border(BorderStroke(1.dp, Color.Gray), shape = RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        if (imageUris.isNotEmpty()) {
            LazyRow {
                items(imageUris) { uri ->
                    val bitmap = MediaStore.Images.Media.getBitmap(LocalContext.current.contentResolver, uri)
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Gambar terpilih",
                        modifier = Modifier
                            .padding(8.dp)
                            .size(160.dp)
                            .clickable { onImageClick(uri) }
                    )
                }
            }
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable(onClick = onAddImageClick)
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


@Preview(showSystemUi = true)
@Composable
fun InputSafetyScreenPreview() {
    InputSafetyPatrolScreen(navController = rememberNavController())
}
