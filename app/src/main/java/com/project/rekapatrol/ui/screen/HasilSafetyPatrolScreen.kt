package com.project.rekapatrol.ui.screen

import android.util.Log
import android.widget.Toast
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.project.rekapatrol.R
import com.project.rekapatrol.data.viewModel.GeneralViewModel
import com.project.rekapatrol.data.viewModelFactory.GeneralViewModelFactory
import com.project.rekapatrol.ui.theme.cream
import com.project.rekapatrol.ui.theme.disabled
import androidx.compose.material.icons.filled.MoreVert
import com.project.rekapatrol.support.TokenHandler

data class SafetyPatrolResult(
    val id: Int,
    val risk: String,
    val date: String,
    val category: String,
    val location: String,
    val isSolved: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HasilSafetyPatrolScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val generalViewModel: GeneralViewModel = viewModel(factory = GeneralViewModelFactory(context))
    val safetyPatrolItems = generalViewModel.safetyPatrolFlow.collectAsLazyPagingItems()

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
                onClick = { generalViewModel.downloadSafetyPatrolRecapExcel() },
                containerColor = cream,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(
                    painterResource(
                        id = R.drawable.download_24px
                    ),
                    contentDescription = "Download"
                )
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
            items(safetyPatrolItems.itemCount) { index ->
                val item = safetyPatrolItems[index]
                item?.let {
                    val inspectionResult = SafetyPatrolResult(
                        id = it.id ?: -1,
                        risk = it.risk ?: "-",
                        date = it.checkupDate ?: "-",
                        category = it.category ?: "-",
                        location = it.location ?: "-",
                        isSolved = !it.actionDescription.isNullOrBlank()
                    )
                    InspectionCard(inspectionResult, navController = navController)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun InspectionCard(result: SafetyPatrolResult, navController: NavController) {
    val context = LocalContext.current
    val tokenHandler = remember { TokenHandler(context) }
    val userRole = tokenHandler.getUserRole()
    var expanded by remember { mutableStateOf(false) }



    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(2.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (result.isSolved) disabled else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    // Keterangan Risiko
                    Text(
                        text = "Risk: ${result.risk}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    // Tanggal
                    Text(
                        text = "Date: ${result.date}",
                        fontSize = 14.sp
                    )

                    // Kategori
                    Text(
                        text = "Cat: ${result.category}",
                        fontSize = 14.sp
                    )
                }

                Box {
                    IconButton(
                        onClick = { if (!result.isSolved) expanded = true },
                        enabled = !result.isSolved
                    ) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        if (userRole == "SHE") {
                            DropdownMenuItem(
                                text = { Text("Edit") },
                                onClick = {
                                    expanded = false
                                    navController.navigate("updateSafetyPatrol/${result.id}")
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Tindak Lanjut") },
                                onClick = {
                                    expanded = false
                                    navController.navigate("detailSafetyPatrol/${result.id}/true")
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Evaluasi") },
                                onClick = {
                                    expanded = false
                                    navController.navigate("#")
                                }
                            )
                        } else if (userRole == "PIC") {
                            DropdownMenuItem(
                                text = { Text("Tindak Lanjut") },
                                onClick = {
                                    expanded = false
                                    navController.navigate("detailSafetyPatrol/${result.id}/true")
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Evaluasi") },
                                onClick = {
                                    expanded = false
                                    navController.navigate("#")
                                }
                            )
                        } else if (userRole == "Manajemen") {
                            DropdownMenuItem(
                                text = { Text("Evaluasi") },
                                onClick = {
                                    expanded = false
                                    navController.navigate("#")
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Lokasi, posisikan di bawah row header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "Lokasi: ${result.location}",
                    fontSize = 14.sp
                )
            }
        }
    }
}




//@Preview(showSystemUi = true)
//@Composable
//fun HasilSafetyPatrolScreenPreview() {
//    HasilSafetyPatrolScreen(navController = rememberNavController())
//}