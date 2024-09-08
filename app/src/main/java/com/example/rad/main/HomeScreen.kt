package com.example.rad.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun HomeScreen(navController: NavController) {
    Column(modifier = Modifier.padding(16.dp)) {
        Button(
            onClick = { navController.navigate("insertOrChange") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Insert or Change Algorithm")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("previewOrDelete") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Preview or Delete Algorithm")
        }
    }
}