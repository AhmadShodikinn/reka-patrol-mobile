package com.project.rekapatrol.ui.screen

import CameraPreviewScreen
import android.app.DatePickerDialog
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
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
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.project.rekapatrol.R
import com.project.rekapatrol.data.response.DataItemCriterias
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
fun DetailInputInspeksiScreen(
    navController: NavController,
    criteriaType: String,
    inspeksiId: Int? = null,
    viewModel: GeneralViewModel = viewModel(factory = GeneralViewModelFactory(LocalContext.current))
) {
    val context = LocalContext.current
    val isEditMode = inspeksiId != null

    // Form state
    var lokasi by remember { mutableStateOf("") }
    var keteranganTemuan by remember { mutableStateOf("") }
    var value by remember { mutableStateOf("") }
    var sustainability by remember { mutableStateOf<Boolean?>(null) }
    var tanggal by remember { mutableStateOf("") }

    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
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
    val sustainabilityOptions = listOf(
        "Ya" to true,
        "Tidak" to false
    )

    var expandedLokasi by remember { mutableStateOf(false) }
    var expandedSustain by remember { mutableStateOf(false) }
    var selectedCriteriaId by remember { mutableStateOf<Int?>(null) }
    var expandedCriteria by remember { mutableStateOf(false) }
    var selectedCriteriaName by remember { mutableStateOf("") }

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

    val currentCriteriaType by rememberUpdatedState(newValue = criteriaType)
    val currentLocationId = lokasiToId[lokasi] ?: 0

    val criteriaPagingItems = remember(currentCriteriaType, currentLocationId) {
        viewModel.getCriteriasPaging(currentCriteriaType, currentLocationId)
    }.collectAsLazyPagingItems()

    val inputResult by viewModel.inputInspeksiResponse.observeAsState()
    val updateResult by viewModel.updateInspectionResponse.observeAsState()

    val detail by viewModel.inspectionDetailResposne.observeAsState()

    LaunchedEffect(inspeksiId) {
        if (inspeksiId != null) {
            viewModel.getDetailInspection(inspeksiId)
            Log.d("Bla", inspeksiId.toString())
        }
    }

    LaunchedEffect(detail) {
        detail?.data?.let {
            lokasi = it.inspectionLocation.toString()
            keteranganTemuan = it.findingsDescription.toString()
            value = it.value.toString()
            sustainability = when (it.suitability) {
                1 -> true
                0 -> false
                else -> null
            }

            tanggal = it.checkupDate.toString()
            selectedCriteriaId = it.criteriaId
            selectedCriteriaName = it.criteria?.criteriaName.toString()
            val baseUrl = "http://192.168.18.5:8001/storage/"
            imageUrl = it.findings?.get(0)?.imagePath?.let { path -> "$baseUrl$path" }
        }
    }

    LaunchedEffect(inputResult) {
        inputResult?.let {
            Toast.makeText(context, "Berhasil input inspeksi!", Toast.LENGTH_SHORT).show()
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
                            if (isEditMode) "Update Inspeksi 5R" else "Input Inspeksi 5R",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = skyblue),
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        if (isImageFullscreen && selectedImageUri != null) {
            //Showing image
            FullscreenImageView(
                imageUri = selectedImageUri!!,
                onClose = { isImageFullscreen = false }
            )
        } else if (isCameraActive) {
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
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ExposedDropdownMenuBox(
                    expanded = expandedLokasi,
                    onExpandedChange = {
                        if (!isEditMode) {
                            expandedLokasi = !expandedLokasi
                        }
                    }
                ) {
                    OutlinedTextField(
                        value = lokasi,
                        onValueChange = {},
                        readOnly = true,
                        enabled = !isEditMode,
                        label = { Text("Lokasi") },
                        trailingIcon = {
                            if (!isEditMode) ExposedDropdownMenuDefaults.TrailingIcon(expandedLokasi)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )

                    if (!isEditMode) {
                        ExposedDropdownMenu(
                            expanded = expandedLokasi,
                            onDismissRequest = { expandedLokasi = false }
                        ) {
                            lokasiOptions.forEach {
                                DropdownMenuItem(
                                    text = { Text(it) },
                                    onClick = {
                                        lokasi = it
                                        expandedLokasi = false
                                    }
                                )
                            }
                        }
                    }
                }


                //kriteria dropdown
                ExposedDropdownMenuBox(
                    expanded = expandedCriteria,
                    onExpandedChange = {
                        if (!isEditMode) {
                            expandedCriteria = !expandedCriteria
                        }
                    }
                ) {
                    OutlinedTextField(
                        value = selectedCriteriaName,
                        onValueChange = {},
                        readOnly = true,
                        enabled = !isEditMode,
                        label = { Text("Kriteria") },
                        trailingIcon = {
                            if (!isEditMode) ExposedDropdownMenuDefaults.TrailingIcon(expandedCriteria)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )

                    if (!isEditMode) {
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

                // Keterangan
                OutlinedTextField(
                    value = keteranganTemuan,
                    onValueChange = { keteranganTemuan = it },
                    label = { Text("Keterangan Temuan") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Value Dropdown
                OutlinedTextField(
                    value = value,
                    onValueChange = { value = it },
                    label = { Text("Nilai") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Sustainability Dropdown
                ExposedDropdownMenuBox(
                    expanded = expandedSustain,
                    onExpandedChange = { expandedSustain = !expandedSustain }
                ) {
                    OutlinedTextField(
                        value = when (sustainability) {
                            true -> "Ya"
                            false -> "Tidak"
                            else -> ""
                        },
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Kesesuaian") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedSustain) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedSustain,
                        onDismissRequest = { expandedSustain = false }
                    ) {
                        sustainabilityOptions.forEach { (label, value) ->
                            DropdownMenuItem(
                                text = { Text(label) },
                                onClick = {
                                    sustainability = value
                                    expandedSustain = false
                                }
                            )
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
                            lokasi.isNotBlank() &&
                            keteranganTemuan.isNotBlank() &&
                            value.isNotBlank() &&
                            tanggal.isNotBlank() &&
                            imageUris.isNotEmpty()
                        ) {
                            val multipartFiles = imageUris.map { uriToMultipartFinding(context, it) }

                            if (isEditMode) {
                                viewModel.updateInspection(
                                    inspectionId = inspeksiId!!,
                                    findingPaths = multipartFiles,
                                    findingsDescription = keteranganTemuan,
                                    inspectionLocation = lokasi,
                                    value = value,
                                    suitability = sustainability == true,
                                    checkupDate = tanggal
                                )
                            } else {
                                viewModel.inputInspeksi(
                                    criteriaId = selectedCriteriaId!!,
                                    findingPaths = multipartFiles,
                                    findingsDescription = keteranganTemuan,
                                    inspectionLocation = lokasi,
                                    value = value,
                                    suitability = sustainability == true,
                                    checkupDate = tanggal
                                )
                            }
                        } else {
                            Toast.makeText(
                                context,
                                "Lengkapi semua data terlebih dahulu",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = skyblue),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        if (isEditMode) "Update" else "Submit",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun DetailInputInspeksiScreenPreview() {
    DetailInputInspeksiScreen(navController = rememberNavController(), "Resik")
}
