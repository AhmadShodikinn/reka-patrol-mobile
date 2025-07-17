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
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.project.rekapatrol.R
import com.project.rekapatrol.data.viewModel.AuthViewModel
import com.project.rekapatrol.data.viewModelFactory.AuthViewModelFactory

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {}
) {
    val context = LocalContext.current

    // ViewModel dari factory
    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(context)
    )

    var nip by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val loginResult by authViewModel.authLoginResult.observeAsState()

    LaunchedEffect(loginResult) {
        loginResult?.let {
            if (it.token != null){
                if (it.user?.positionId != 1) {
                    onLoginSuccess()
                }
            } else {
                Toast.makeText(context, "Login gagal!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Gambar dekoratif atas & bawah
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
                .padding(24.dp, 40.dp, 24.dp, 10.dp)
        ) {
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

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Person, contentDescription = "User Icon", tint = Color.Gray)
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = nip,
                        onValueChange = { nip = it },
                        label = { Text("NIP") },
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier.fillMaxWidth().height(60.dp),
                        textStyle = LocalTextStyle.current.copy(fontSize = MaterialTheme.typography.bodyMedium.fontSize),
                        singleLine = true
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Lock, contentDescription = "Password Icon", tint = Color.Gray)
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier.fillMaxWidth().height(60.dp),
                        textStyle = LocalTextStyle.current.copy(fontSize = MaterialTheme.typography.bodyMedium.fontSize),
                        singleLine = true
                    )
                }

                Button(
                    onClick = {
                        authViewModel.login(nip, password)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9AD0D3)),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Text("Login", color = Color.White)
                }

                TextButton(
                    onClick = onForgotPasswordClick,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(
                        text = "Lupa password?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}


//@Preview(showSystemUi = true)
//@Composable
//fun LoginScreenPreview() {
//    LoginScreen()
//}

