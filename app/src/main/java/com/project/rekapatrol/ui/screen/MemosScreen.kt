package com.project.rekapatrol.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.project.rekapatrol.R
import com.project.rekapatrol.data.response.DocumentItem
import com.project.rekapatrol.data.viewModel.GeneralViewModel
import com.project.rekapatrol.data.viewModelFactory.GeneralViewModelFactory
import com.project.rekapatrol.ui.theme.cream
import com.project.rekapatrol.ui.theme.skyblue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemosScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val generalViewModel: GeneralViewModel = viewModel(factory = GeneralViewModelFactory(context))
    val documentItems = generalViewModel.getDocumentFlow("memo").collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Memo",
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
                }
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            items(documentItems.itemCount) {index ->
                val item = documentItems[index]
                item?.let {
                    val document = documentItems[index]
                    document?.let {
                        MemoCard(
                            item = DocumentItem(
                                id = it.id ?: -1,
                                title = it.fileName ?: "Tanpa Judul",
                                date = it.createdAt ?: "Tanggal tidak tersedia",
                                imageRes = R.drawable.baseline_source_24
                            ),
                            onClick = {
                                generalViewModel.downloadDocument(context, id = it.id ?: -1)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MemoCard(
    item: DocumentItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = skyblue),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val image: Painter = painterResource(id = item.imageRes)
            Image(
                painter = image,
                contentDescription = "Ikon Dokumen",
                modifier = Modifier
                    .size(50.dp)
                    .aspectRatio(1f)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxHeight()
            ) {
                Text(
                    text = item.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.date.take(10),
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

