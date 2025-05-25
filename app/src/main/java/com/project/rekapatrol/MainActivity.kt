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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.project.rekapatrol.support.TokenHandler
import com.project.rekapatrol.ui.screen.*
import com.project.rekapatrol.ui.theme.RekapatrolTheme

class MainActivity : ComponentActivity() {

    // Menambahkan ActivityResultLauncher untuk meminta izin
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Kamera diizinkan!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Kamera tidak diizinkan!", Toast.LENGTH_SHORT).show()
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

        // ==== STORAGE PERMISSION (Android 9 dan di bawah) ====
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1001
                )
            }
        }


        val tokenHandler = TokenHandler(this)
        val token = tokenHandler.getToken()

        // Izin sudah diberikan, bisa lanjutkan
        if (token == null) {
            setContent {
                RekapatrolTheme {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        AppNavigator(tokenExists = false)
                    }
                }
            }
        } else {
            // Jika token ada, lanjutkan aplikasi ke layar utama
            setContent {
                RekapatrolTheme {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        AppNavigator(tokenExists = true)
                    }
                }
            }
        }
    }

    @Composable
    fun AppNavigator(tokenExists: Boolean) {
        val navController = rememberNavController()

        NavHost(navController = navController, startDestination = if (tokenExists) "mainMenu" else "loginScreen") {
            composable("loginScreen") {
                LoginScreen(onLoginSuccess = {
                    navController.navigate("mainMenu") {
                        popUpTo("loginScreen") { inclusive = true }
                    }
                })
            }

            composable("mainMenu") {
                MainMenuScreen(
                    onNavigate = { destination -> navController.navigate(destination) },
                    onLogoutSuccess = {
                        navController.navigate("loginScreen") {
                            popUpTo("mainMenu") { inclusive = true }
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

            composable("tindakLanjutInspeksi/{id}") { backStackEntry ->
                val inspectionId = backStackEntry.arguments?.getString("id")?.toIntOrNull()
                inspectionId?.let {
                    TindakLanjutInspeksiScreen(navController = navController, inspectionId = it)
                } }

            composable("jsa") { JSAScreen(navController = navController) }
            composable("peraturan") { PeraturanScreen(navController = navController) }

            composable("detailInputInspeksi/{kriteria}") { backStackEntry ->
                val kriteria = backStackEntry.arguments?.getString("kriteria")
                kriteria?.let {
                    DetailInputInspeksiScreen(criteriaType = it, navController = navController)
                }
            }

            composable(
                route = "updateInspeksi/{kriteria}/{inspeksiId}",
                arguments = listOf(
                    navArgument("kriteria") { type = NavType.StringType },
                    navArgument("inspeksiId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val kriteria = backStackEntry.arguments?.getString("kriteria") ?: ""
                val inspeksiId = backStackEntry.arguments?.getInt("inspeksiId") ?: -1

                DetailInputInspeksiScreen(
                    criteriaType = kriteria,
                    inspeksiId = if (inspeksiId == -1) null else inspeksiId,
                    navController = navController
                )
            }

            composable(
                route = "updateSafetyPatrol/{safetyPatrolId}",
                arguments = listOf(
                    navArgument("safetyPatrolId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val safetyPatrolId = backStackEntry.arguments?.getInt("safetyPatrolId") ?: -1

                InputSafetyPatrolScreen(
                    safetyPatrolId = if (safetyPatrolId == -1) null else safetyPatrolId,
                    navController = navController
                )
            }
        }
    }
}
