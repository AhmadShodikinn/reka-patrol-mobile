package com.project.rekapatrol.ui.screen

import CameraPreviewScreen
import android.app.DatePickerDialog
import android.net.Uri
import android.provider.MediaStore
import android.widget.DatePicker
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
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.project.rekapatrol.R
import com.project.rekapatrol.data.response.DataItemCriterias
import com.project.rekapatrol.data.viewModel.GeneralViewModel
import com.project.rekapatrol.data.viewModelFactory.GeneralViewModelFactory
import com.project.rekapatrol.ui.helper.uriToMultipartFinding
import com.project.rekapatrol.ui.theme.cream
import com.project.rekapatrol.ui.theme.skyblue
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailInputInspeksiScreen(
    navController: NavController,
    criteriaType: String,
    viewModel: GeneralViewModel = viewModel(factory = GeneralViewModelFactory(LocalContext.current))
) {
    val context = LocalContext.current

    // Form state
    var lokasi by remember { mutableStateOf("") }
    var keteranganTemuan by remember { mutableStateOf("") }
    var value by remember { mutableStateOf("") }
    var sustainability by remember { mutableStateOf("") }
    var tanggal by remember { mutableStateOf("") }

    // Gambar
    var showDialog by remember { mutableStateOf(false) }
    var imageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }
    var isCameraActive by remember { mutableStateOf(false) }

    // Date Picker
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

    val lokasiOptions = listOf("Workshop/Gudang", "Kantor")
    val lokasiToId = mapOf("Workshop/Gudang" to 1, "Kantor" to 2)
    val valueOptions = listOf("Nilai 1", "Nilai 2", "Nilai 3")
    val sustainabilityOptions = listOf("Sustainable", "Tidak Sustainable")

    var expandedLokasi by remember { mutableStateOf(false) }
    var expandedValue by remember { mutableStateOf(false) }
    var expandedSustain by remember { mutableStateOf(false) }
    var selectedCriteriaId by remember { mutableStateOf<Int?>(null) }
    var expandedCriteria by remember { mutableStateOf(false) }
    var selectedCriteriaName by remember { mutableStateOf("") }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        imageUris = uris
    }

    val currentCriteriaType by rememberUpdatedState(newValue = criteriaType)
    val currentLocationId = lokasiToId[lokasi] ?: 0
    val criteriaPagingItems = remember(currentCriteriaType, currentLocationId) {
        viewModel.getCriteriasPaging(currentCriteriaType, currentLocationId)
    }.collectAsLazyPagingItems()
    val result by viewModel.inputInspeksiResponse.observeAsState()


    LaunchedEffect(result) {
        result?.let {
            Toast.makeText(context, "Berhasil input inspeksi!", Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            if (!isCameraActive) {
                CenterAlignedTopAppBar(
                    title = { Text("Input Inspeksi", fontSize = 20.sp, fontWeight = FontWeight.Medium, color = Color.White) },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = skyblue),
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        if (isCameraActive) {
            CameraPreviewScreen(
                onImageCaptured = { uri ->
                    imageUris = listOf(uri)
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
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ExposedDropdownMenuBox(expanded = expandedLokasi, onExpandedChange = { expandedLokasi = !expandedLokasi }) {
                    OutlinedTextField(
                        value = lokasi,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Lokasi") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedLokasi) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(expanded = expandedLokasi, onDismissRequest = { expandedLokasi = false }) {
                        lokasiOptions.forEach {
                            DropdownMenuItem(text = { Text(it) }, onClick = {
                                lokasi = it
                                expandedLokasi = false
//                                val locationId = lokasiToId[it]
//                                if (locationId != null) {
//                                    viewModel.loadCriterias(criteriaType, locationId)
//                                }
                            })
                        }
                    }
                }

                //kriteria dropdown
                ExposedDropdownMenuBox(
                    expanded = expandedCriteria,
                    onExpandedChange = { expandedCriteria = !expandedCriteria }
                ) {
                    OutlinedTextField(
                        value = selectedCriteriaName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Kriteria") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedCriteria) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenuBox(
                        expanded = expandedCriteria,
                        onExpandedChange = { expandedCriteria = !expandedCriteria }
                    ) {
                        OutlinedTextField(
                            value = selectedCriteriaName,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Kriteria") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedCriteria) },
                            modifier = Modifier.menuAnchor().fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = expandedCriteria,
                            onDismissRequest = { expandedCriteria = false }
                        ) {
                            for (i in 0 until criteriaPagingItems.itemCount) {
                                val item = criteriaPagingItems[i]
                                if (item != null) {
                                    DropdownMenuItem(
                                        text = { Text(item.criteriaName ?: "Tanpa Nama") },
                                        onClick = {
                                            selectedCriteriaName = item.criteriaName ?: ""
                                            selectedCriteriaId = item.id
                                            expandedCriteria = false
                                        }
                                    )
                                }
                            }

                        }
                    }
                }

                // Gambar
                ImagePickerSectionForInputInspeksi(imageUris = imageUris) { showDialog = true }

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

                // Keterangan
                OutlinedTextField(
                    value = keteranganTemuan,
                    onValueChange = { keteranganTemuan = it },
                    label = { Text("Keterangan Temuan") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Value Dropdown
                ExposedDropdownMenuBox(expanded = expandedValue, onExpandedChange = { expandedValue = !expandedValue }) {
                    OutlinedTextField(
                        value = value,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Value") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedValue) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(expanded = expandedValue, onDismissRequest = { expandedValue = false }) {
                        valueOptions.forEach {
                            DropdownMenuItem(text = { Text(it) }, onClick = {
                                value = it
                                expandedValue = false
                            })
                        }
                    }
                }

                // Sustainability Dropdown
                ExposedDropdownMenuBox(expanded = expandedSustain, onExpandedChange = { expandedSustain = !expandedSustain }) {
                    OutlinedTextField(
                        value = sustainability,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Sustainability") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedSustain) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(expanded = expandedSustain, onDismissRequest = { expandedSustain = false }) {
                        sustainabilityOptions.forEach {
                            DropdownMenuItem(text = { Text(it) }, onClick = {
                                sustainability = it
                                expandedSustain = false
                            })
                        }
                    }
                }

                // Tanggal
                OutlinedTextField(
                    value = tanggal,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Tanggal Pemeriksaan") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { datePickerDialog.show() },
                    enabled = false
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Submit
                Button(
                    onClick = {
                        if (
                            selectedCriteriaId != null &&
                            lokasi.isNotBlank() && keteranganTemuan.isNotBlank() &&
                            value.isNotBlank() && sustainability.isNotBlank() &&
                            tanggal.isNotBlank() && imageUris.isNotEmpty()
                        ) {
                            val multipartFiles = imageUris.map { uriToMultipartFinding(context, it) }

                            viewModel.inputInspeksi(
                                criteriaId = selectedCriteriaId!!,
                                findingPaths = multipartFiles,
                                findingsDescription = keteranganTemuan,
                                inspectionLocation = lokasi,
                                value = value,
                                suitability = sustainability,
                                checkupDate = tanggal
                            )
                        } else {
                            Toast.makeText(context, "Lengkapi semua data terlebih dahulu", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = skyblue),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Submit", color = Color.White, fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
fun ImagePickerSectionForInputInspeksi(imageUris: List<Uri>, onClick: () -> Unit) {
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
fun DetailInputInspeksiScreenPreview() {
    DetailInputInspeksiScreen(navController = rememberNavController(), "Resik")
}
