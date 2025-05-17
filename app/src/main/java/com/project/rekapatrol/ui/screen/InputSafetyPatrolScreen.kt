package com.project.rekapatrol.ui.screen

import android.app.DatePickerDialog
import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.project.rekapatrol.R
import com.project.rekapatrol.data.repository.Repository
import com.project.rekapatrol.data.viewModel.GeneralViewModel
import com.project.rekapatrol.data.viewModelFactory.GeneralViewModelFactory
import com.project.rekapatrol.ui.theme.cream
import com.project.rekapatrol.ui.theme.skyblue
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputSafetyPatrolScreen(navController: NavController) {
    val context = LocalContext.current

    val generalViewModel: GeneralViewModel = viewModel(
        factory = GeneralViewModelFactory(context)
    )

    var temuan by remember { mutableStateOf("") }
    var lokasi by remember { mutableStateOf("") }
    var kategori by remember { mutableStateOf("") }
    var resiko by remember { mutableStateOf("") }
    var tanggal by remember { mutableStateOf("") }

    val kategoriOptions = listOf("UC", "CA")
    val resikoOptions = listOf("Rendah", "Sedang", "Tinggi")

    var expandedKategori by remember { mutableStateOf(false) }
    var expandedResiko by remember { mutableStateOf(false) }

    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            tanggal = "$year-${month + 1}-$dayOfMonth"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    val result by generalViewModel.inputSafetyPatrolsResponse.observeAsState()

    LaunchedEffect(result) {
        result?.let {
            Toast.makeText(context, "Berhasil submit data!", Toast.LENGTH_SHORT).show()
            navController.popBackStack() // Kembali ke screen sebelumnya
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Input Safety Patrol", color = Color.White)
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = cream
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
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

            // Dummy gambar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clickable {
                        // Nanti buka kamera atau galeri
                        Toast
                            .makeText(context, "Klik gambar untuk pilih dari kamera/galeri", Toast.LENGTH_SHORT)
                            .show()
                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_documentjsa), // Gambar placeholder
                    contentDescription = "Upload Gambar",
                    modifier = Modifier
                        .size(120.dp)
                )
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
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
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
            OutlinedTextField(
                value = tanggal,
                onValueChange = {},
                readOnly = true,
                label = { Text("Tanggal Pemeriksaan") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        Toast.makeText(context, "Opening Date Picker", Toast.LENGTH_SHORT).show()
                        datePickerDialog.show()
                    }
            )

            Spacer(modifier = Modifier.height(24.dp))
//token e gaada kang
            Button(
                onClick = {
                    if (temuan.isNotBlank() && lokasi.isNotBlank() && kategori.isNotBlank() &&
                        resiko.isNotBlank() && tanggal.isNotBlank()
                    ) {
                        generalViewModel.inputSafetyPatrol(
                            finding_path = listOf("dummy_path.jpg"), // bisa kamu ganti dengan URI nanti
                            finding_description = temuan,
                            location = lokasi,
                            category = kategori,
                            risk = resiko,
                            checkup_date = tanggal
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
                Text("Submit", color = Color.White)
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun InputSafetyScreenPreview() {
    InputSafetyPatrolScreen(navController = rememberNavController())
}
