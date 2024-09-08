package com.example.rad.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Theme Switcher and Navigation") },
                actions = {
                    Switch(
                        checked = isDarkTheme,
                        onCheckedChange = { onThemeToggle() }
                    )
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(navController)
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
        }
    }
}