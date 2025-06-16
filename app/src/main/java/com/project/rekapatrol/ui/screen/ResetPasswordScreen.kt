package com.project.rekapatrol.ui.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.project.rekapatrol.R
import com.project.rekapatrol.data.viewModel.AuthViewModel
import com.project.rekapatrol.data.viewModelFactory.AuthViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    onNavigateBack: () -> Unit = {}
) {
    val context = LocalContext.current

    // ViewModel dari factory (tetap sama seperti di LoginScreen)
    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(context)
    )

    var nip by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val resetPasswordResult by authViewModel.resetPasswordResult.observeAsState()

    LaunchedEffect(resetPasswordResult) {
        resetPasswordResult?.let { result ->
            Toast.makeText(context, result.message, Toast.LENGTH_LONG).show()
            if (result.success) {
                onNavigateBack()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg_vector1),
            contentDescription = "Vector Atas",
            modifier = Modifier
                .height(80.dp)
                .align(Alignment.TopEnd)
        )

        Image(
            painter = painterResource(id = R.drawable.bg_vector2),
            contentDescription = "Vector Bawah",
            modifier = Modifier
                .height(80.dp)
                .align(Alignment.BottomStart)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp, 40.dp, 24.dp, 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Reset Password",
                    fontSize = 24.sp,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                    modifier = Modifier.padding(top = 8.dp),
                    color = Color.Black
                )
                Text(
                    text = "Masukkan NIP dan password baru Anda.",
                    fontSize = 16.sp,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp, bottom = 20.dp),
                    color = Color.Gray
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                // Input NIP
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Person, contentDescription = "NIP Icon", tint = Color.Gray)
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = nip,
                        onValueChange = { nip = it },
                        label = { Text("NIP") },
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        textStyle = LocalTextStyle.current.copy(fontSize = MaterialTheme.typography.bodyMedium.fontSize),
                        singleLine = true
                    )
                }

                // Input Password Baru
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Lock, contentDescription = "New Password Icon", tint = Color.Gray)
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        label = { Text("Password Baru") },
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        textStyle = LocalTextStyle.current.copy(fontSize = MaterialTheme.typography.bodyMedium.fontSize),
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation() // Untuk menyembunyikan password
                    )
                }

                // Input Konfirmasi Password
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Lock, contentDescription = "Confirm Password Icon", tint = Color.Gray)
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = { Text("Konfirmasi Password") },
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        textStyle = LocalTextStyle.current.copy(fontSize = MaterialTheme.typography.bodyMedium.fontSize),
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation() // Untuk menyembunyikan password
                    )
                }

                Button(
                    onClick = {
                        if (nip.isNotBlank() && newPassword.isNotBlank() && confirmPassword.isNotBlank()) {
                            if (newPassword == confirmPassword) {
                               authViewModel.resetPassword(nip, newPassword, confirmPassword)
                            } else {
                                Toast.makeText(context, "Konfirmasi password tidak cocok!", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(context, "Harap lengkapi semua bidang.", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9AD0D3)),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Text("Submit", color = Color.White)
                }

                TextButton(
                    onClick = { onNavigateBack() },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Kembali ke Login", color = Color.Gray)
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun ForgotPasswordScreenPreview() {
    ForgotPasswordScreen()
}