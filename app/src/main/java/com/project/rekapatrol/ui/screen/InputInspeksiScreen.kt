package com.project.rekapatrol.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.project.rekapatrol.ui.theme.cream
import com.project.rekapatrol.ui.theme.skyblue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputInspeksiScreen(navController: NavController) {
    // Mengatur state untuk tombol yang dipilih
    var selectedKriteria by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Pilih Kriteria Inspeksi",
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
                }
            )
        },
        containerColor = Color.White
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Menampilkan tombol-tombol untuk memilih kriteria
            val kriteriaList = listOf("Ringkas", "Rapi", "Resik", "Rawat", "Rajin")

            kriteriaList.forEach { kriteria ->
                Button(
                    onClick = {
                        selectedKriteria = kriteria
                        // Arahkan ke halaman detail input inspeksi dengan membawa kriteria yang dipilih
                        navController.navigate("detailInputInspeksi/$kriteria")
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
                        text = kriteria,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                }
            }

            // Menampilkan kriteria yang dipilih
            selectedKriteria?.let {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Kriteria Terpilih: $it",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun InputInspeksiScreenPreview() {
    // Gantikan dengan NavController saat menggunakan di aplikasi sebenarnya
    InputInspeksiScreen(navController = rememberNavController())
}
