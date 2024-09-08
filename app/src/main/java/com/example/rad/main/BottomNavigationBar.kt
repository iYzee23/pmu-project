package com.example.rad.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(navController: NavController) {
    val currentRoute = navController.currentBackStackEntryAsState()?.value?.destination?.route

    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Main Screen") },
            label = { Text("Home") },
            selected = currentRoute == "home",
            onClick = {
                navController.navigate("home") {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true
                    }
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Star, contentDescription = "Visualizer") },
            label = { Text("Visualizer") },
            selected = currentRoute == "visualizer",
            onClick = {
                navController.navigate("visualizer") {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true
                    }
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Info, contentDescription = "Information and Quiz") },
            label = { Text("Info & Quiz") },
            selected = currentRoute == "informationAndQuiz",
            onClick = {
                navController.navigate("informationAndQuiz") {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true
                    }
                }
            }
        )
    }
}