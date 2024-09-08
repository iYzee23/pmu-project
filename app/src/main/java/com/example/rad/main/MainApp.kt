package com.example.rad.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.rad.algorithm.AlgorithmViewModel
import com.example.rad.chatgpt.ChatViewModel
import com.example.rad.database.DatabaseViewModel
import com.example.rad.ui.theme.RadTheme

@Composable
fun MainApp(
    databaseViewModel: DatabaseViewModel,
    viewModel: AlgorithmViewModel,
    chatViewModel: ChatViewModel
) {
    var isDarkTheme by remember { mutableStateOf(false) }

    RadTheme(darkTheme = isDarkTheme) {
        MainScreen(
            databaseViewModel = databaseViewModel,
            viewModel = viewModel,
            chatViewModel = chatViewModel,
            isDarkTheme = isDarkTheme,
            onThemeToggle = { isDarkTheme = !isDarkTheme }
        )
    }
}