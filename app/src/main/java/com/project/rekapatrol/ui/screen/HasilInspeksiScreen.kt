package com.project.rekapatrol.ui.screen

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import com.project.rekapatrol.ui.theme.cream
import com.project.rekapatrol.ui.theme.disabled
import com.project.rekapatrol.ui.theme.skyblue
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.LaunchedEffect
import com.project.rekapatrol.support.TokenHandler
import com.project.rekapatrol.ui.helper.createPdfMultipart
import java.util.Calendar

data class InspeksiResult(
    val id: Int,
    val criteriaId: Int,
    val keterangan: String,
    val lokasi: String,
    val isSolved: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HasilInspeksiScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val generalViewModel: GeneralViewModel = viewModel(factory = GeneralViewModelFactory(context))
    val inspeksiItems = generalViewModel.inspeksiFlow.collectAsLazyPagingItems()

    var showFilterDialog by remember { mutableStateOf(false) }
    var selectedMonth by remember { mutableStateOf<Int?>(null) }
    var selectedYear by remember { mutableStateOf<Int?>(null) }

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
                        Icon(Icons.Default.ArrowBack, contentDescription = "Filter")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { generalViewModel.downloadInspectionRecapExcel() },
                containerColor = skyblue,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(painterResource(id = R.drawable.download_24px), contentDescription = "Download")
            }
        },
        containerColor = Color.White
    ) { paddingValues ->
        if (selectedMonth != null || selectedYear != null) {
            FilterIndicator(
                selectedMonth = selectedMonth,
                selectedYear = selectedYear,
                onClearFilter = {
                    selectedMonth = null
                    selectedYear = null
                }
            )
        }

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
                        lokasi = it.inspectionLocation ?: "-",
                        isSolved = !it.actionDescription.isNullOrBlank()
                    )
                    InspeksiCard(
                        item = inspeksiResult,
                        navController = navController,
                        launcher = launcher,
                        onDeleteConfirmed = { inspectionId ->
                            generalViewModel.deleteInspection(inspectionId)
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        if (showFilterDialog) {
            FilterDialog(
                currentMonth = selectedMonth,
                currentYear = selectedYear,
                onDismiss = { showFilterDialog = false },
                onApplyFilter = { month, year ->
                    selectedMonth = month
                    selectedYear = year
                    showFilterDialog = false
                }
            )
        }
    }
}

@Composable
fun FilterIndicator(
    selectedMonth: Int?,
    selectedYear: Int?,
    onClearFilter: () -> Unit
) {
    val monthNames = listOf(
        "Januari", "Februari", "Maret", "April", "Mei", "Juni",
        "Juli", "Agustus", "September", "Oktober", "November", "Desember"
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        color = skyblue.copy(alpha = 0.1f),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row {
                Text(
                    text = "Filter: ",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = buildString {
                        if (selectedMonth != null) {
                            append(monthNames[selectedMonth - 1])
                        }
                        if (selectedMonth != null && selectedYear != null) {
                            append(" ")
                        }
                        if (selectedYear != null) {
                            append(selectedYear)
                        }
                    },
                    fontSize = 14.sp,
                    color = skyblue,
                    fontWeight = FontWeight.Bold
                )
            }

            TextButton(onClick = onClearFilter) {
                Text(
                    text = "Hapus",
                    color = skyblue,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterDialog(
    currentMonth: Int?,
    currentYear: Int?,
    onDismiss: () -> Unit,
    onApplyFilter: (month: Int?, year: Int?) -> Unit
) {
    var selectedMonth by remember { mutableStateOf(currentMonth) }
    var selectedYear by remember { mutableStateOf(currentYear) }
    var showMonthDropdown by remember { mutableStateOf(false) }
    var showYearDropdown by remember { mutableStateOf(false) }

    val monthNames = listOf(
        "Januari", "Februari", "Maret", "April", "Mei", "Juni",
        "Juli", "Agustus", "September", "Oktober", "November", "Desember"
    )

    val currentCalendar = Calendar.getInstance()
    val currentRealYear = currentCalendar.get(Calendar.YEAR)
    val yearRange = (currentRealYear - 10)..(currentRealYear + 10)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Filter Inspeksi",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                // Dropdown untuk memilih bulan
                ExposedDropdownMenuBox(
                    expanded = showMonthDropdown,
                    onExpandedChange = { showMonthDropdown = !showMonthDropdown }
                ) {
                    OutlinedTextField(
                        value = selectedMonth?.let { monthNames[it - 1] } ?: "Semua Bulan",
                        onValueChange = { },
                        readOnly = true,
                        label = { Text("Bulan") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = showMonthDropdown)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = showMonthDropdown,
                        onDismissRequest = { showMonthDropdown = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Semua Bulan") },
                            onClick = {
                                selectedMonth = null
                                showMonthDropdown = false
                            }
                        )
                        monthNames.forEachIndexed { index, month ->
                            DropdownMenuItem(
                                text = { Text(month) },
                                onClick = {
                                    selectedMonth = index + 1
                                    showMonthDropdown = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                ExposedDropdownMenuBox(
                    expanded = showYearDropdown,
                    onExpandedChange = { showYearDropdown = !showYearDropdown }
                ) {
                    OutlinedTextField(
                        value = selectedYear?.toString() ?: "Semua Tahun",
                        onValueChange = { },
                        readOnly = true,
                        label = { Text("Tahun") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = showYearDropdown)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = showYearDropdown,
                        onDismissRequest = { showYearDropdown = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Semua Tahun") },
                            onClick = {
                                selectedYear = null
                                showYearDropdown = false
                            }
                        )
                        yearRange.forEach { year ->
                            DropdownMenuItem(
                                text = { Text(year.toString()) },
                                onClick = {
                                    selectedYear = year
                                    showYearDropdown = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onApplyFilter(selectedMonth, selectedYear) },
                colors = ButtonDefaults.buttonColors(containerColor = skyblue)
            ) {
                Text("Terapkan", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal", color = skyblue)
            }
        }
    )
}




@Composable
fun InspeksiCard(
    item: InspeksiResult,
    navController: NavController,
    launcher: ManagedActivityResultLauncher<String, Uri?>,
    onDeleteConfirmed: (Int) -> Unit
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

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(2.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (item.isSolved) disabled else Color.White
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
                                text = { Text("Edit Temuan") },
                                onClick = {
                                    expanded = false
                                    navController.navigate("updateInspeksi/${item.criteriaId}/${item.id}")
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Tindak Lanjut") },
                                onClick = {
                                    expanded = false
                                    navController.navigate("detailInspeksi/${item.criteriaId}/${item.id}/true")
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Evaluasi Temuan") },
                                onClick = {
                                    expanded = false
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
                                text = { Text("Edit Temuan") },
                                onClick = {
                                    expanded = false
                                    navController.navigate("updateInspeksi/${item.criteriaId}/${item.id}")
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Tindak Lanjut") },
                                onClick = {
                                    expanded = false
                                    navController.navigate("detailInspeksi/${item.criteriaId}/${item.id}/true")
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Evaluasi Temuan") },
                                onClick = {
                                    expanded = false
                                    launcher.launch("application/pdf")
                                }
                            )
                        } else if (userRole == "PIC") {
                            DropdownMenuItem(
                                text = { Text("Evaluasi Temuan") },
                                onClick = {
                                    expanded = false
                                    launcher.launch("application/pdf")
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Tindak Lanjut") },
                                onClick = {
                                    expanded = false
                                    navController.navigate("detailInspeksi/${item.criteriaId}/${item.id}/true")
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
