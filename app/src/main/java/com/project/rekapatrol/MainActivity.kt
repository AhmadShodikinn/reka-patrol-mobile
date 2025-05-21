package com.project.rekapatrol

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.project.rekapatrol.ui.screen.*
import com.project.rekapatrol.ui.theme.RekapatrolTheme

class MainActivity : ComponentActivity() {

    // Menambahkan ActivityResultLauncher untuk meminta izin
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission granted!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Memeriksa apakah izin sudah diberikan
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
        } else {
            // Izin belum diberikan, kita meminta izin
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }

        // Izin sudah diberikan, bisa lanjutkan
        setContent {
            RekapatrolTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigator()
                }
            }
        }
    }

    @Composable
    fun AppNavigator() {
        val navController = rememberNavController()

        NavHost(navController = navController, startDestination = "loginScreen" ) {
            composable("loginScreen") {
                LoginScreen(onLoginSuccess = {
                    navController.navigate("mainMenu") {
                        popUpTo("login") { inclusive = true }
                    }
                })
            }

            composable("mainMenu") {
                MainMenuScreen(
                    onNavigate = { destination -> navController.navigate(destination) },
                    onLogoutSuccess = {
                        navController.navigate("login") {
                            popUpTo("mainMenu") { inclusive = true } // Supaya back tidak kembali ke mainMenu
                        }
                    }
                )
            }

            composable("inputSafetyPatrol") { InputSafetyPatrolScreen(navController = navController) }
            composable("hasilSafetyPatrol") { HasilSafetyPatrolScreen(navController = navController) }
            composable("tindakLanjutSafetyPatrol/{id}") { backStackEntry ->
                val patrolId = backStackEntry.arguments?.getString("id")?.toIntOrNull()
                patrolId?.let {
                    TindakLanjutSafetyPatrolScreen(navController = navController, safetyPatrolId = it)
                }
            }
            composable("inputInspeksi") { InputInspeksiScreen(navController = navController) }
            composable("hasilInspeksi") { HasilInspeksiScreen(navController = navController) }
            composable("tindakLanjutInspeksi") { TindakLanjutInspeksiScreen(navController = navController) }
            composable("jsa") { JSAScreen(navController = navController) }

            composable("detailInputInspeksi/{kriteria}") { backStackEntry ->
                val kriteria = backStackEntry.arguments?.getString("kriteria")
                kriteria?.let {
                    DetailInputInspeksiScreen(criteriaType = it, navController = navController)
                }
            }
        }
    }
}
