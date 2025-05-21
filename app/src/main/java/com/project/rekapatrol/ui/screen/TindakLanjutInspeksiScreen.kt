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
import androidx.compose.ui.graphics.asImageBitmap
import com.project.rekapatrol.R
import com.project.rekapatrol.data.viewModel.GeneralViewModel
import com.project.rekapatrol.data.viewModelFactory.GeneralViewModelFactory
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
    val criteriaName = inspectionDetail?.data?.criteria?.criteriaName ?: "Memuat kriteria..."

    // State untuk input dan gambar
    var tindakLanjut by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var imageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }
    var isCameraActive by remember { mutableStateOf(false) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    val result by generalViewModel.updateInspectionResponse.observeAsState()

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        imageUris = uris
    }

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

    Scaffold(
        topBar = {
            if (!isCameraActive) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "Tindak Lanjut Inspeksi Ringkas",
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
        if (isCameraActive) {
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
                    text = "Kriteria:",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = criteriaName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(2.dp))

                // Gambar
                ImagePickerSectionForTindakLanjutInspeksi(
                    imageUris = imageUris,
                    onClick = { showDialog = true }
                )

                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        title = { Text("Pilih Gambar") },
                        text = { Text("Pilih sumber gambar:") },
                        confirmButton = {
                            TextButton(onClick = {
                                showDialog = false
                                isCameraActive = true
                            }) {
                                Text("Kamera")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = {
                                showDialog = false
                                galleryLauncher.launch("image/*")
                            }) {
                                Text("Galeri")
                            }
                        }
                    )
                }

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
                            generalViewModel.updateInspection(
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

@Composable
fun ImagePickerSectionForTindakLanjutInspeksi(imageUris: List<Uri>, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(Color(0xFFEFEFEF), shape = RoundedCornerShape(8.dp))
            .border(BorderStroke(1.dp, Color.Gray), shape = RoundedCornerShape(8.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (imageUris.isNotEmpty()) {
            LazyRow {
                items(imageUris) { uri ->
                    val bitmap = MediaStore.Images.Media.getBitmap(LocalContext.current.contentResolver, uri)
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Gambar",
                        modifier = Modifier
                            .fillMaxHeight()
                            .aspectRatio(16 / 9f)
                    )
                }
            }
        } else {
            Image(
                painter = painterResource(id = R.drawable.imagesmode),
                contentDescription = "Placeholder",
                modifier = Modifier.size(120.dp)
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun TindakLanjutInspeksiScreenPreview() {
    TindakLanjutInspeksiScreen(navController = rememberNavController(), 1)
}
