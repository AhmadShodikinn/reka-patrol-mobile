package com.project.rekapatrol.ui.screen

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.project.rekapatrol.R
import com.project.rekapatrol.data.viewModel.GeneralViewModel
import com.project.rekapatrol.data.viewModelFactory.GeneralViewModelFactory
import com.project.rekapatrol.ui.theme.disabled
import com.project.rekapatrol.ui.theme.skyblue
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import com.project.rekapatrol.support.TokenHandler
import com.project.rekapatrol.ui.helper.FilterDialog
import com.project.rekapatrol.ui.helper.createPdfMultipart

data class InspeksiResult(
    val id: Int,
    val criteriaId: Int,
    val keterangan: String,
    val isValidEntry: Boolean?,
    val lokasi: String,
    val isSolved: Boolean,
    val hasMemo: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HasilInspeksiScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val tokenHandler = remember { TokenHandler(context) }
    val userRole = tokenHandler.getUserRole()
    val generalViewModel: GeneralViewModel = viewModel(factory = GeneralViewModelFactory(context))

    var showFilterDialog by remember { mutableStateOf(false) }
    var fromDate by remember { mutableStateOf<String?>(null) }
    var toDate by remember { mutableStateOf<String?>(null) }
    var selectedStatus by remember { mutableStateOf<String?>(null) }

    val inspeksiItems = generalViewModel.getInspeksiFlow(fromDate, toDate, selectedStatus).collectAsLazyPagingItems()
    var onEvaluasiUploadId by remember { mutableStateOf<Int?>(null) }

    val uploadResult by generalViewModel.uploadMemosResult.observeAsState()
    var memoId by remember { mutableStateOf<Int?>(null) }


    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            val filePart = createPdfMultipart(context, uri)
            if (filePart != null) {
                generalViewModel.uploadMemos(filePart)
            } else {
                Toast.makeText(context, "Gagal memuat file", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Tidak ada file yang dipilih", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(uploadResult) {
        uploadResult?.data?.id?.let {
            memoId = it

            onEvaluasiUploadId?.let { evaluasiId ->
                generalViewModel.updateInspectionHasMemo(
                    inspectionId = evaluasiId,
                    hasMemo = it,
                    onComplete = {
                        Toast.makeText(context, "Berhasil menandai temuan memiliki memo.", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Hasil Inspeksi 5R",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = skyblue,
                    titleContentColor = Color.Black,
                    navigationIconContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                },
                actions = {
                    IconButton(onClick = { showFilterDialog = true }) {
                        Icon(
                            painter = painterResource(id = R.drawable.date_range),
                            contentDescription = "Filter"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            if (userRole == "SHE" || userRole == "5R") {
                FloatingActionButton(
                    onClick = { generalViewModel.downloadInspectionRecapExcel() },
                    containerColor = skyblue,
                    contentColor = Color.White,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(
                        painterResource(id = R.drawable.download_24px),
                        contentDescription = "Download"
                    )
                }
            }
        },
        containerColor = Color.White
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            items(inspeksiItems.itemCount) { index ->
                val item = inspeksiItems[index]
                item?.let {
                    val inspeksiResult = InspeksiResult(
                        id = it.id ?: -1,
                        criteriaId = it.criteriaId ?: -1,
                        keterangan = it.findingsDescription ?: "-",
                        isValidEntry = when (it.isValidEntry) {
                            "1" -> true
                            "0" -> false
                            else -> null
                        },
                        lokasi = it.inspectionLocation ?: "-",
                        isSolved = !it.actionDescription.isNullOrBlank(),
                        hasMemo = it.hasMemo ?: false
                    )
                    InspeksiCard(
                        item = inspeksiResult,
                        navController = navController,
                        launcher = launcher,
                        onDeleteConfirmed = { inspectionId ->
                            generalViewModel.deleteInspection(inspectionId)
                        },
                        onValidEntryUpdate = { id, isValid ->
                            generalViewModel.updateIsValidEntryInspection(id, isValid) {
                                inspeksiItems.refresh()
                            }
                        },
                        onEvaluasiSelected = { inspectionId ->
                            onEvaluasiUploadId = inspectionId
                            launcher.launch("application/pdf")
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        if (showFilterDialog) {
            FilterDialog(
                currentFrom = fromDate,
                currentTo = toDate,
                currentStatus = selectedStatus,
                onDismiss = { showFilterDialog = false },
                onApplyFilter = { f, t, s ->
                    fromDate = f
                    toDate = t
                    selectedStatus = s
                    showFilterDialog = false
                }
            )
        }
    }
}


@Composable
fun InspeksiCard(
    item: InspeksiResult,
    navController: NavController,
    launcher: ManagedActivityResultLauncher<String, Uri?>,
    onDeleteConfirmed: (Int) -> Unit,
    onValidEntryUpdate: (safetyPatrolId: Int, isValid: Boolean) -> Unit,
    onEvaluasiSelected: (Int) -> Unit,
) {
    val context = LocalContext.current
    val tokenHandler = remember { TokenHandler(context) }
    val userRole = tokenHandler.getUserRole()
    var expanded by remember { mutableStateOf(false) }
    var shouldLaunchPicker by remember { mutableStateOf(false) }


    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(shouldLaunchPicker) {
        if (shouldLaunchPicker) {
            launcher.launch("application/pdf")
            shouldLaunchPicker = false
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Konfirmasi Penghapusan") },
            text = { Text("Apakah Anda yakin ingin menghapus temuan ini?") },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteConfirmed(item.id)
                        showDeleteDialog = false
                        expanded = false
                    }
                ) {
                    Text("Hapus")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false }
                ) {
                    Text("Batal")
                }
            }
        )
    }

    val containerColor = when {
        item.hasMemo == true -> Color.Yellow
        item.isSolved -> disabled
        item.isValidEntry == true -> Color.Green
        item.isValidEntry == false -> Color.Red
        else -> Color.White
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(2.dp),
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = item.keterangan,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f)
                )

                Box {
                    IconButton(
                        onClick = { if (!item.isSolved) expanded = true },
                        enabled = !item.isSolved
                    ) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        if (userRole == "SHE") {
                            DropdownMenuItem(
                                text = { Text("Revisi Temuan") },
                                onClick = {
                                    expanded = false
                                    navController.navigate("updateInspeksi/${item.criteriaId}/${item.id}")
                                }
                            )

                            when (item.isValidEntry) {
                                null -> {
                                    DropdownMenuItem(
                                        text = { Text("Konfirmasi Temuan") },
                                        onClick = {
                                            expanded = false
                                            onValidEntryUpdate(item.id, true)
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Tolak Konfirmasi Temuan") },
                                        onClick = {
                                            expanded = false
                                            onValidEntryUpdate(item.id, false)
                                        }
                                    )
                                }
                                true -> {
                                    DropdownMenuItem(
                                        text = { Text("Tindak Lanjut") },
                                        onClick = {
                                            expanded = false
                                            navController.navigate("detailInspeksi/${item.criteriaId}/${item.id}/true")
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Tolak Konfirmasi Temuan") },
                                        onClick = {
                                            expanded = false
                                            onValidEntryUpdate(item.id, false)
                                        }
                                    )
                                }
                                false -> {
                                    DropdownMenuItem(
                                        text = { Text("Konfirmasi Temuan") },
                                        onClick = {
                                            expanded = false
                                            onValidEntryUpdate(item.id, true)
                                        }
                                    )
                                }
                            }

                            DropdownMenuItem(
                                text = { Text("Evaluasi Temuan") },
                                onClick = {
                                    expanded = false
                                    onEvaluasiSelected(item.id)
                                    launcher.launch("application/pdf")
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Hapus Temuan") },
                                onClick = {
                                    showDeleteDialog = true
                                }
                            )
                        } else if (userRole == "5R") {
                            DropdownMenuItem(
                                text = { Text("Revisi Temuan") },
                                onClick = {
                                    expanded = false
                                    navController.navigate("updateInspeksi/${item.criteriaId}/${item.id}")
                                }
                            )

                            when (item.isValidEntry) {
                                null -> {
                                    DropdownMenuItem(
                                        text = { Text("Konfirmasi Temuan") },
                                        onClick = {
                                            expanded = false
                                            onValidEntryUpdate(item.id, true)
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Tolak Konfirmasi Temuan") },
                                        onClick = {
                                            expanded = false
                                            onValidEntryUpdate(item.id, false)
                                        }
                                    )
                                }
                                true -> {
                                    DropdownMenuItem(
                                        text = { Text("Tindak Lanjut") },
                                        onClick = {
                                            expanded = false
                                            navController.navigate("detailInspeksi/${item.criteriaId}/${item.id}/true")
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Tolak Konfirmasi Temuan") },
                                        onClick = {
                                            expanded = false
                                            onValidEntryUpdate(item.id, false)
                                        }
                                    )
                                }
                                false -> {
                                    DropdownMenuItem(
                                        text = { Text("Konfirmasi Temuan") },
                                        onClick = {
                                            expanded = false
                                            onValidEntryUpdate(item.id, true)
                                        }
                                    )
                                }
                            }

                            DropdownMenuItem(
                                text = { Text("Evaluasi Temuan") },
                                onClick = {
                                    expanded = false
                                    onEvaluasiSelected(item.id)
                                    launcher.launch("application/pdf")
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Hapus Temuan") },
                                onClick = {
                                    showDeleteDialog = true
                                }
                            )
                        } else if (userRole == "PIC") {

                            when (item.isValidEntry) {
                                null -> {
                                    DropdownMenuItem(
                                        text = { Text("Konfirmasi Temuan") },
                                        onClick = {
                                            expanded = false
                                            onValidEntryUpdate(item.id, true)
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Tolak Konfirmasi Temuan") },
                                        onClick = {
                                            expanded = false
                                            onValidEntryUpdate(item.id, false)
                                        }
                                    )
                                }
                                true -> {
                                    DropdownMenuItem(
                                        text = { Text("Tindak Lanjut") },
                                        onClick = {
                                            expanded = false
                                            navController.navigate("detailInspeksi/${item.criteriaId}/${item.id}/true")
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Tolak Konfirmasi Temuan") },
                                        onClick = {
                                            expanded = false
                                            onValidEntryUpdate(item.id, false)
                                        }
                                    )
                                }
                                false -> {
                                    DropdownMenuItem(
                                        text = { Text("Konfirmasi Temuan") },
                                        onClick = {
                                            expanded = false
                                            onValidEntryUpdate(item.id, true)
                                        }
                                    )
                                }
                            }

                            DropdownMenuItem(
                                text = { Text("Evaluasi Temuan") },
                                onClick = {
                                    expanded = false
                                    onEvaluasiSelected(item.id)
                                    launcher.launch("application/pdf")
                                }
                            )

                        } else if (userRole == "Manajemen") {
                            DropdownMenuItem(
                                text = { Text("Evaluasi Temuan") },
                                onClick = {
                                    expanded = false
                                    launcher.launch("application/pdf")
                                }
                            )

                        }

                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Lokasi: ${item.lokasi}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun HasilInspeksiScreenPreview() {
    HasilInspeksiScreen(navController = rememberNavController())
}
