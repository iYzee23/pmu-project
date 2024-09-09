package com.example.rad.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(navController: NavController, isDarkTheme: Boolean) {
    val currentRoute = navController.currentBackStackEntryAsState()?.value?.destination?.route

    val backgroundColor = if (isDarkTheme) Color(0xFF121212) else Color(0xFFEDE7F6)
    val contentColor = if (isDarkTheme) Color.White else Color.DarkGray

    NavigationBar(
        tonalElevation = 6.dp,
        containerColor = backgroundColor,
        contentColor = contentColor
    ) {
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
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF673AB7),
                selectedTextColor = Color(0xFF673AB7),
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color(0xFFB39DDB)
            )
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
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF673AB7),
                selectedTextColor = Color(0xFF673AB7),
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color(0xFFB39DDB)
            )
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
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF673AB7),
                selectedTextColor = Color(0xFF673AB7),
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color(0xFFB39DDB)
            )
        )
    }
}
