package com.project.rekapatrol.ui.screen

import android.app.DatePickerDialog
import android.widget.DatePicker
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
import com.project.rekapatrol.R
import com.project.rekapatrol.ui.theme.cream
import com.project.rekapatrol.ui.theme.skyblue
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputSafetyPatrolScreen(onBack: () -> Unit = {}) {
    val context = LocalContext.current

    var temuan by remember { mutableStateOf("") }
    var lokasi by remember { mutableStateOf("") }
    var kategori by remember { mutableStateOf("") }
    var resiko by remember { mutableStateOf("") }
    var tanggal by remember { mutableStateOf("") }

    val kategoriOptions = listOf("Kebersihan", "Peralatan", "Keselamatan")
    val resikoOptions = listOf("Rendah", "Sedang", "Tinggi")

    var expandedKategori by remember { mutableStateOf(false) }
    var expandedResiko by remember { mutableStateOf(false) }

    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, day: Int ->
            tanggal = "$day/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Input Safety Patrol",
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
                    IconButton(onClick = { onBack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                },
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.Start
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

            // Gambar Dummy Upload
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clickable {
                        // Di sini nanti bisa pakai image picker
                    },
                contentAlignment = Alignment.CenterStart
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_documentjsa), // placeholder image
                    contentDescription = "Upload Gambar",
                    modifier = Modifier.size(100.dp)
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
                    modifier = Modifier.fillMaxWidth()
                        .menuAnchor()
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
                    modifier = Modifier.fillMaxWidth()
                        .menuAnchor()
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
                        datePickerDialog.show()
                    }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    // Handle simpan / submit data
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = skyblue,
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


@Preview(showSystemUi = true)
@Composable
fun InputSafetyScreenPreview() {
    InputSafetyPatrolScreen()
}