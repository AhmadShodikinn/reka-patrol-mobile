package com.project.rekapatrol.ui.screen

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.project.rekapatrol.R
import com.project.rekapatrol.ui.theme.cream
import com.project.rekapatrol.ui.theme.skyblue
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailInputInspeksiScreen(kriteria: String, navController: NavController) {
    val context = LocalContext.current

    // Form state
    var kriteria by remember { mutableStateOf("") }
    var lokasi by remember { mutableStateOf("") }
    var keteranganTemuan by remember { mutableStateOf("") }
    var value by remember { mutableStateOf("") }
    var sustainability by remember { mutableStateOf("") }
    var tanggal by remember { mutableStateOf("") }

    // Dropdown options
    val kriteriaOptions = listOf("Kebersihan", "Peralatan", "Keselamatan")
    val lokasiOptions = listOf("Area A", "Area B", "Area C")
    val valueOptions = listOf("Nilai 1", "Nilai 2", "Nilai 3")
    val sustainabilityOptions = listOf("Sustainable", "Tidak Sustainable")

    // Date Picker Dialog
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
                    containerColor = skyblue,
                    titleContentColor = Color.Black,
                    navigationIconContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
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
            // Dropdown Kriteria
            ExposedDropdownMenuBox(
                expanded = kriteria.isNotEmpty(),
                onExpandedChange = { kriteria = if (kriteria.isEmpty()) "Kebersihan" else "" }
            ) {
                OutlinedTextField(
                    value = kriteria,
                    onValueChange = { },
                    readOnly = true,
                    label = { Text("Kriteria") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = kriteria.isNotEmpty()) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = kriteria.isNotEmpty(),
                    onDismissRequest = { },
                ) {
                    kriteriaOptions.forEach {
                        DropdownMenuItem(
                            text = { Text(it) },
                            onClick = {
                                kriteria = it
                            }
                        )
                    }
                }
            }

            // Dropdown Lokasi
            ExposedDropdownMenuBox(
                expanded = lokasi.isNotEmpty(),
                onExpandedChange = { lokasi = if (lokasi.isEmpty()) "Area A" else "" }
            ) {
                OutlinedTextField(
                    value = lokasi,
                    onValueChange = { },
                    readOnly = true,
                    label = { Text("Lokasi") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = lokasi.isNotEmpty()) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = lokasi.isNotEmpty(),
                    onDismissRequest = { },
                ) {
                    lokasiOptions.forEach {
                        DropdownMenuItem(
                            text = { Text(it) },
                            onClick = {
                                lokasi = it
                            }
                        )
                    }
                }
            }

            // Gambar Upload
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clickable {
                        // Image Picker Logic Here
                    },
                contentAlignment = Alignment.CenterStart
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_documentjsa), // placeholder image
                    contentDescription = "Upload Gambar",
                    modifier = Modifier.size(100.dp)
                )
            }

            // Keterangan Temuan
            OutlinedTextField(
                value = keteranganTemuan,
                onValueChange = { keteranganTemuan = it },
                label = { Text("Keterangan Temuan") },
                modifier = Modifier.fillMaxWidth()
            )

            // Dropdown Value
            ExposedDropdownMenuBox(
                expanded = value.isNotEmpty(),
                onExpandedChange = { value = if (value.isEmpty()) "Nilai 1" else "" }
            ) {
                OutlinedTextField(
                    value = value,
                    onValueChange = { },
                    readOnly = true,
                    label = { Text("Value") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = value.isNotEmpty()) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = value.isNotEmpty(),
                    onDismissRequest = { },
                ) {
                    valueOptions.forEach {
                        DropdownMenuItem(
                            text = { Text(it) },
                            onClick = {
                                value = it
                            }
                        )
                    }
                }
            }

            // Sustainability
            ExposedDropdownMenuBox(
                expanded = sustainability.isNotEmpty(),
                onExpandedChange = { sustainability = if (sustainability.isEmpty()) "Sustainable" else "" }
            ) {
                OutlinedTextField(
                    value = sustainability,
                    onValueChange = { },
                    readOnly = true,
                    label = { Text("Sustainability") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = sustainability.isNotEmpty()) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = sustainability.isNotEmpty(),
                    onDismissRequest = { },
                ) {
                    sustainabilityOptions.forEach {
                        DropdownMenuItem(
                            text = { Text(it) },
                            onClick = {
                                sustainability = it
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

@Preview(showSystemUi = true)
@Composable
fun DetailInputInspeksiScreenPreview() {
    DetailInputInspeksiScreen(kriteria = "Ringkas", navController = rememberNavController())
}
