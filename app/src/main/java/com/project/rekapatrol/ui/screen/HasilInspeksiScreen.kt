package com.project.rekapatrol.ui.screen

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.project.rekapatrol.R
import com.project.rekapatrol.ui.theme.cream
import com.project.rekapatrol.ui.theme.disabled
import com.project.rekapatrol.ui.theme.skyblue

// Dummy data model
data class InspeksiResult(
    val keterangan: String,
    val lokasi: String,
    val isSolved: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HasilInspeksiScreen(
    navController: NavController,
    onAddClick: () -> Unit = {}
) {
    val inspeksiResults = listOf(
        InspeksiResult(
            "Tidak ada barang yang tidak diperlukan di area mesin/tempat kerja.",
            "Workshop", true
        ),
        InspeksiResult(
            "Peralatan tidak tertata rapi di rak.",
            "Gudang", false
        ),
        InspeksiResult(
            "Papan instruksi tidak terlihat jelas.",
            "Workshop", false
        )
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Hasil Inspeksi",
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
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAddClick() },
                containerColor = skyblue,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(
                    painterResource(
                        id = R.drawable.download_24px
                    ),
                    contentDescription = "Tambah"
                )
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
            items(inspeksiResults) { item ->
                InspeksiCard(item, navController = navController)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun InspeksiCard(item: InspeksiResult, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate("tindakLanjutInspeksi")
            },
        shape = RoundedCornerShape(2.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (item.isSolved) disabled else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {
            Text(
                text = item.keterangan,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "Lokasi: ${item.lokasi}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun HasilInspeksiScreenPreview() {
    HasilInspeksiScreen(navController = rememberNavController())
}
