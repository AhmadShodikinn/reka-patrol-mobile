package com.project.rekapatrol.ui.screen

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
import com.project.rekapatrol.support.TokenHandler

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
                    InspeksiCard(item = inspeksiResult, navController = navController)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun InspeksiCard(item: InspeksiResult, navController: NavController) {
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
                        } else {
                            DropdownMenuItem(
                                text = { Text("Tindak Lanjut") },
                                onClick = {
                                    expanded = false
                                    navController.navigate("detailInspeksi/${item.criteriaId}/${item.id}/true")
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
