package com.project.rekapatrol.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.rekapatrol.R
import com.project.rekapatrol.ui.theme.cream

@Composable
fun MainMenuScreen(onNavigate: (String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Image(
            painter = painterResource(id = R.mipmap.bg_illustration2_foreground),
            contentDescription = "Vector Gelombang Atas",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .align(Alignment.TopCenter)
        )

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
                .padding(top=120.dp, start = 15.dp, end = 15.dp)
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
                text = "Temuan belum ditutup : 10",
                fontSize = 15.sp,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                modifier = Modifier
                    .align(Alignment.Start),
                color = Color.Black
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 90.dp)
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
                                onClick = { onNavigate("inputInspeksi") }
                            )
                            MenuButton(
                                iconId = R.drawable.ic_documentjsa,
                                label = "Dokumen JSA",
                                onClick = { onNavigate("inputInspeksi") }
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
                                label = "Input Inspeksi Patrol",
                                onClick = { onNavigate("inputSafetyPatrol") }
                            )
                            MenuButton(
                                iconId = R.drawable.ic_patrol2,
                                label = "Hasil Inspeksi Patrol",
                                onClick = { onNavigate("inputSafetyPatrol") }
                            )
                            MenuButton(
                                iconId = R.drawable.ic_exit,
                                label = "Keluar",
                                onClick = { onNavigate("inputSafetyPatrol") }
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
                modifier = Modifier
                    .fillMaxSize(),
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



//@Preview(showSystemUi = true)
//@Composable
//fun MainMenuPreview() {
//    MainMenuScreen()
//}

