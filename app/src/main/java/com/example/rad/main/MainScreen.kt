package com.example.rad.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rad.algorithm.AlgorithmViewModel
import com.example.rad.chatgpt.ChatViewModel
import com.example.rad.database.DatabaseViewModel
import com.example.rad.database.InformationAndQuizScreen
import com.example.rad.database.InsertOrChangeAlgorithm
import com.example.rad.database.PreviewOrDeleteAlgorithm
import com.example.rad.database.VisualizerScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    databaseViewModel: DatabaseViewModel,
    viewModel: AlgorithmViewModel,
    chatViewModel: ChatViewModel,
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit
) {
    val navController = rememberNavController()
    val currentRoute = remember { mutableStateOf("home") }

    LaunchedEffect(navController) {
        navController.currentBackStackEntryFlow.collect { backStackEntry ->
            currentRoute.value = backStackEntry.destination.route ?: "home"
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Algo Visualizer",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        ),
                        color = Color.White
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (currentRoute.value == "settings") {
                                navController.popBackStack()
                            } else {
                                navController.navigate("settings")
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (currentRoute.value == "settings") Icons.Default.ArrowBack else Icons.Default.Settings,
                            contentDescription = if (currentRoute.value == "settings") "Back" else "Settings",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color(0xFF9575CD)
                )
            )
        },
        bottomBar = {
            BottomNavigationBar(navController, isDarkTheme)
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(it)
        ) {
            composable("home") {
                HomeScreen(navController = navController)
            }
            composable("insertOrChange") {
                InsertOrChangeAlgorithm(databaseViewModel = databaseViewModel, viewModel = viewModel)
            }
            composable("previewOrDelete") {
                PreviewOrDeleteAlgorithm(databaseViewModel = databaseViewModel, viewModel = viewModel)
            }
            composable("visualizer") {
                VisualizerScreen(databaseViewModel = databaseViewModel, viewModel = viewModel)
            }
            composable("informationAndQuiz") {
                InformationAndQuizScreen(
                    databaseViewModel = databaseViewModel,
                    chatViewModel = chatViewModel,
                    viewModel = viewModel
                )
            }
            composable("settings") {
                SettingsScreen(isDarkTheme = isDarkTheme, onThemeToggle = onThemeToggle)
            }
        }
    }
}

