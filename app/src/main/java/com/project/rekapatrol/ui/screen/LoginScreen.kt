package com.project.rekapatrol.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.project.rekapatrol.R

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Gambar Gelombang di bagian atas kanan (di luar Box utama)
        Image(
            painter = painterResource(id = R.drawable.bg_vector1),
            contentDescription = "Vector Gelombang Atas",
            modifier = Modifier
                .height(80.dp)
                .align(Alignment.TopEnd)
        )

        Image(
            painter = painterResource(id = R.drawable.bg_vector2),
            contentDescription = "Vector Gelombang Bawah",
            modifier = Modifier
                .height(80.dp)
                .align(Alignment.BottomStart)
        )

        // Kolom Konten Utama
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp, 40.dp, 24.dp, 10.dp)
        ) {
            // Middle: Ilustrasi (Placeholder)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.mipmap.bg_illustration_foreground),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .width(460.dp)
                        .height(350.dp)
                )

                Text(
                    text = "Selamat Datang",
                    fontSize = 24.sp,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                    modifier = Modifier.padding(top = 8.dp),
                    color = Color.Black
                )
            }
            // Bottom: Form Login
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),

            ) {
                // Username
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Person, contentDescription = "User Icon", tint = Color.Gray)
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        label = { Text("Email") },
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        textStyle = LocalTextStyle.current.copy(fontSize = MaterialTheme.typography.bodyMedium.fontSize),
                        singleLine = true,
                    )
                }

                // Password
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Lock, contentDescription = "Password Icon", tint = Color.Gray)
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        label = { Text("Password") },
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        textStyle = LocalTextStyle.current.copy(fontSize = MaterialTheme.typography.bodyMedium.fontSize),
                        singleLine = true,
                    )
                }

                // Tombol Login
                Button(
                    onClick = { onLoginSuccess() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9AD0D3)),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Text("Login", color = Color.White)
                }

                // Lupa Password
                Text(
                    text = "Lupa password?",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.End),
                    color = Color.Gray
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}

