package com.project.rekapatrol.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.project.rekapatrol.R
import com.project.rekapatrol.ui.theme.cream
import com.project.rekapatrol.ui.theme.skyblue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TindakLanjutSafetyPatrolScreen(navController: NavController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Tindak Lanjut Safety Patrol",
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
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
        ) {

            val image: Painter = painterResource(id = R.drawable.bg_vector1) // Replace with actual image resource
            Image(
                painter = image,
                contentDescription = "Safety Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp) // Gambar besar dan lebar
            )
            Spacer(modifier = Modifier.height(2.dp))
            // Tindak Lanjut
            Text(
                text = "Tindak Lanjut:",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

            // Text Area Input
            val textState = remember { androidx.compose.runtime.mutableStateOf("") }

            BasicTextField(
                value = textState.value,
                onValueChange = { textState.value = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .border(1.dp, Color.Gray, RoundedCornerShape(2.dp)),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        // Handle keyboard action if necessary
                    }
                )
            )
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    // Handle simpan / submit data
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = cream,
                    contentColor = Color.Black
                )
            ) {
                Text(
                    text = "Submit",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun TindakLanjutSafetyPatrolScreenPreview() {
    TindakLanjutSafetyPatrolScreen(navController = rememberNavController())
}
