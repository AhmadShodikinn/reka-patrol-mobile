package com.project.rekapatrol

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.project.rekapatrol.ui.screen.DetailInputInspeksiScreen
import com.project.rekapatrol.ui.screen.HasilInspeksiScreen
import com.project.rekapatrol.ui.screen.HasilSafetyPatrolScreen
import com.project.rekapatrol.ui.screen.InputInspeksiScreen
import com.project.rekapatrol.ui.screen.InputSafetyPatrolScreen
import com.project.rekapatrol.ui.screen.JSAScreen
import com.project.rekapatrol.ui.screen.LoginScreen
import com.project.rekapatrol.ui.screen.MainMenuScreen
import com.project.rekapatrol.ui.screen.TindakLanjutSafetyPatrolScreen
import com.project.rekapatrol.ui.theme.RekapatrolTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                        popUpTo("login") { inclusive = true } // optional: remove login from backstack
                    }
                })
            }

            composable("mainMenu") {
                MainMenuScreen(
                    onNavigate = { route ->
                        navController.navigate(route)
                    }
                )
            }

            composable("inputSafetyPatrol") { InputSafetyPatrolScreen() }
            composable("hasilSafetyPatrol") { HasilSafetyPatrolScreen() }
            composable("tindakLanjutSafetyPatrol") { TindakLanjutSafetyPatrolScreen() }
            composable("inputInspeksi") { InputInspeksiScreen(navController = navController) }
            composable("hasilInspeksi") { HasilInspeksiScreen() }
            composable("jsa") { JSAScreen() }

            // Menambahkan rute untuk halaman detail input inspeksi
            composable("detailInputInspeksi/{kriteria}") { backStackEntry ->
                val kriteria = backStackEntry.arguments?.getString("kriteria")
                kriteria?.let {
                    DetailInputInspeksiScreen(kriteria = it)
                }
            }
        }
    }

}