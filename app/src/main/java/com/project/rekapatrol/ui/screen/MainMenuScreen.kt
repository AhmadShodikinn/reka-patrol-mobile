package com.project.rekapatrol.ui.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.project.rekapatrol.R
import com.project.rekapatrol.data.viewModel.GeneralViewModel
import com.project.rekapatrol.data.viewModelFactory.GeneralViewModelFactory
import com.project.rekapatrol.support.TokenHandler
import com.project.rekapatrol.ui.theme.cream

@Composable
fun MainMenuScreen(
    onNavigate: (String) -> Unit,
    onLogoutSuccess: () -> Unit
) {
    val context = LocalContext.current
    val generalViewModel: GeneralViewModel = viewModel(factory = GeneralViewModelFactory(context))

    val logoutResult by generalViewModel.authLogoutResult.observeAsState()
    val totalUnsolved by generalViewModel.totalUnsolved.observeAsState(0)

    LaunchedEffect(Unit) {
        generalViewModel.getInformationDashboard()
    }

    LaunchedEffect(logoutResult) {
        logoutResult?.let {
            val tokenHandler = TokenHandler(context)
            tokenHandler.removeToken()

            Toast.makeText(context, "Logout berhasil!", Toast.LENGTH_SHORT).show()
            onLogoutSuccess()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        ) {
            Image(
                painter = painterResource(id = R.mipmap.bg_illustration2_foreground),
                contentDescription = "Vector Gelombang Atas",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
            )

            IconButton(
                onClick = { generalViewModel.logout() },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(20.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_logout),
                    contentDescription = "Logout",
                    tint = Color.Black
                )
            }
        }

        Image(
            painter = painterResource(id = R.drawable.bg_vector3),
            contentDescription = "Vector Gelombang Bawah",
            modifier = Modifier
                .height(80.dp)
                .align(Alignment.BottomStart)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 120.dp, start = 15.dp, end = 15.dp, bottom = 90.dp)
                .align(Alignment.BottomCenter),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "DASHBOARD",
                fontSize = 24.sp,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .align(Alignment.CenterHorizontally),
                color = Color.Black
            )

            Text(
                buildAnnotatedString {
                    append("Temuan belum ditutup: ")
                    withStyle(style = SpanStyle(color = Color.Red)) {
                        append(totalUnsolved.toString())
                    }
                },
                fontSize = 15.sp,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                modifier = Modifier.align(Alignment.Start),
                color = Color.Black // default untuk teks utama
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .border(1.dp, cream, RoundedCornerShape(8.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f),
                            verticalArrangement = Arrangement.SpaceEvenly,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            MenuButton(
                                iconId = R.drawable.ic_inspeksi1,
                                label = "Input Inspeksi 5R",
                                onClick = { onNavigate("inputInspeksi") }
                            )
                            MenuButton(
                                iconId = R.drawable.ic_inspeksi2,
                                label = "Hasil Inspeksi 5R",
                                onClick = { onNavigate("hasilInspeksi") }
                            )
                            MenuButton(
                                iconId = R.drawable.ic_documentjsa,
                                label = "Dokumen JSA",
                                onClick = { onNavigate("jsa") }
                            )
                        }

                        Column(
                            modifier = Modifier
                                .weight(1f),
                            verticalArrangement = Arrangement.SpaceEvenly,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            MenuButton(
                                iconId = R.drawable.ic_patrol1,
                                label = "Input Safety Patrol",
                                onClick = { onNavigate("inputSafetyPatrol") }
                            )
                            MenuButton(
                                iconId = R.drawable.ic_patrol2,
                                label = "Hasil Safety Patrol",
                                onClick = { onNavigate("hasilSafetyPatrol") }
                            )
                            MenuButton(
                                iconId = R.drawable.ic_documentrules,
                                label = "Peraturan K3",
                                onClick = { onNavigate("peraturan") }
                            )
                        }
                    }
                }
            }

        }
    }
}

@Composable
fun MenuButton(iconId: Int, label: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .size(150.dp)
            .clickable { onClick() },
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(Color.Gray, shape = CircleShape)
                .align(Alignment.CenterHorizontally),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = iconId),
                contentDescription = label,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillWidth
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Black,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun MainMenuPreview() {
//    MainMenuScreen(
//        onNavigate = {},
//        onLogoutSuccess = {}
//    )
//    MainMenuUiOnly()
}

@Composable
fun MainMenuUiOnly(
    totalUnsolved: Int = 5,
    onNavigate: (String) -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Gambar atas dengan tombol logout
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        ) {
            Image(
                painter = painterResource(id = R.mipmap.bg_illustration2_foreground),
                contentDescription = "Vector Gelombang Atas",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
            )

            IconButton(
                onClick = onLogoutClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(20.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.download_24px),
                    contentDescription = "Logout",
                    tint = Color.Black
                )
            }
        }

        // Gambar bawah
        Image(
            painter = painterResource(id = R.drawable.bg_vector3),
            contentDescription = "Vector Gelombang Bawah",
            modifier = Modifier
                .height(80.dp)
                .align(Alignment.BottomStart)
        )

        // Konten tengah
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 120.dp, start = 15.dp, end = 15.dp, bottom = 90.dp)
                .align(Alignment.BottomCenter),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "DASHBOARD",
                fontSize = 24.sp,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .align(Alignment.CenterHorizontally),
                color = Color.Black
            )

            Text(
                buildAnnotatedString {
                    append("Temuan belum ditutup: ")
                    withStyle(style = SpanStyle(color = Color.Red)) {
                        append(totalUnsolved.toString())
                    }
                },
                fontSize = 15.sp,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                modifier = Modifier.align(Alignment.Start),
                color = Color.Black
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .border(1.dp, cream, RoundedCornerShape(8.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.SpaceEvenly,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            MenuButton(R.drawable.ic_inspeksi1, "Input Inspeksi 5R", { onNavigate("inputInspeksi") })
                            MenuButton(R.drawable.ic_inspeksi2, "Hasil Inspeksi 5R", { onNavigate("hasilInspeksi") })
                            MenuButton(R.drawable.ic_documentjsa, "Dokumen JSA", { onNavigate("jsa") })
                        }
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.SpaceEvenly,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            MenuButton(R.drawable.ic_patrol1, "Input Inspeksi Patrol", { onNavigate("inputSafetyPatrol") })
                            MenuButton(R.drawable.ic_patrol2, "Hasil Inspeksi Patrol", { onNavigate("hasilSafetyPatrol") })
                            MenuButton(R.drawable.ic_documentjsa, "Peraturan K3", { onNavigate("peraturan") })
                        }
                    }
                }
            }
        }
    }
}

