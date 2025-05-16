package com.project.rekapatrol.ui.screen

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.project.rekapatrol.ui.theme.cream
import com.project.rekapatrol.ui.theme.disabled
import com.project.rekapatrol.ui.theme.skyblue
import com.project.rekapatrol.ui.theme.whiteblue

//sementara
data class InspectionResult(
    val risk: String,
    val date: String,
    val category: String,
    val location: String,
    val isSolved: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HasilSafetyPatrolScreen(
    navController: NavController,
    onAddClick: () -> Unit = {}
) {
    val inspectionResults = listOf(
        InspectionResult("Tinggi", "12/05/2025", "UC", "Area Workshop", true),
        InspectionResult("Sedang", "11/05/2025", "UC", "Area Kantor", false),
        InspectionResult("Rendah", "10/05/2025", "UC", "Area Workshop", false),
        InspectionResult("Tinggi", "09/05/2025", "UC", "Area Kantor", true)
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Hasil Safety Patrol",
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
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAddClick() },
                containerColor = cream,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah")
            }
        },
        containerColor = Color.White
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(inspectionResults) { result ->
                InspectionCard(result)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun InspectionCard(result: InspectionResult) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(2.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (result.isSolved) disabled else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            // Risk
            Text(
                text = "Risk: ${result.risk}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            // Date
            Text(
                text = "Date: ${result.date}",
                fontSize = 14.sp
            )

            // Category
            Text(
                text = "Cat: ${result.category}",
                fontSize = 14.sp
            )

            // Location
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Lokasi: ${result.location}",
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun HasilSafetyPatrolScreenPreview() {
    HasilSafetyPatrolScreen(navController = rememberNavController())
}