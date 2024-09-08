package com.example.rad.database

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.rad.algorithm.AlgorithmViewModel
import com.example.rad.components.VisComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VisualizerScreen(
    databaseViewModel: DatabaseViewModel,
    viewModel: AlgorithmViewModel
) {
    var inputArray by remember { mutableStateOf("") }
    var selectedAlgorithmName by remember { mutableStateOf("") }
    var algorithmNames by remember { mutableStateOf<List<String>>(emptyList()) }
    var isDropdownExpanded by remember { mutableStateOf(false) }
    var result by remember { mutableStateOf("") }

    // Fetch all available algorithms from the database
    LaunchedEffect(Unit) {
        databaseViewModel.getAllAlgorithmNames { names ->
            algorithmNames = names
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        // Input array field (CSV format)
        OutlinedTextField(
            value = inputArray,
            onValueChange = { inputArray = it },
            label = { Text("Input Array (CSV format)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Dropdown for selecting an algorithm
        ExposedDropdownMenuBox(
            expanded = isDropdownExpanded,
            onExpandedChange = { isDropdownExpanded = !isDropdownExpanded }
        ) {
            OutlinedTextField(
                value = selectedAlgorithmName,
                onValueChange = {},
                label = { Text("Select Algorithm") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth().menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = isDropdownExpanded,
                onDismissRequest = { isDropdownExpanded = false }
            ) {
                algorithmNames.forEach { name ->
                    DropdownMenuItem(
                        text = { Text(name) },
                        onClick = {
                            selectedAlgorithmName = name
                            isDropdownExpanded = false
                            databaseViewModel.getAlgorithm(name) { algorithm ->
                                viewModel.updateAlgorithmCode(algorithm?.code ?: "")
                            }
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Button to run the algorithm
        Button(
            onClick = {
                val inputArrayParsed = parseInputArray(inputArray)
                if (inputArrayParsed != null) {
                    viewModel.runPythonCode(viewModel.algorithmCode.value, inputArrayParsed) { resultText ->
                        result = resultText
                        Log.d("usp", resultText)
                    }
                } else {
                    result = "Invalid input array format."
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Load Algorithm and Run")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Visualizer component
        VisComponent(viewModel = viewModel)

        Spacer(modifier = Modifier.height(16.dp))

        // Display result if needed
        if (result.isNotEmpty()) {
            Text(text = "Result: $result", color = if (result == "No errors found.") Color.Green else Color.Red)
        }
    }
}

// Function to parse CSV input array
private fun parseInputArray(inputArrayStr: String): IntArray? {
    return try {
        inputArrayStr.split(",").map { it.trim().toInt() }.toIntArray()
    } catch (e: Exception) {
        null
    }
}
