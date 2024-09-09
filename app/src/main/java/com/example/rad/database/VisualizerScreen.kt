package com.example.rad.database

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
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

    LaunchedEffect(Unit) {
        databaseViewModel.getAllAlgorithmNames { names ->
            algorithmNames = names
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = inputArray,
            onValueChange = { inputArray = it },
            label = { Text("Input Array (CSV format)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

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

        Button(
            onClick = {
                val inputArrayParsed = parseInputArray(inputArray)
                if (inputArrayParsed != null) {
                    viewModel.runPythonCode(viewModel.algorithmCode.value, inputArrayParsed) { resultText ->
                        result = ""
                        Log.d("usp", resultText)
                    }
                } else if (selectedAlgorithmName == "") {
                    result = "You must select algorithm."
                } else {
                    result = "Invalid input array format."
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Load Algorithm and Run")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (result.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, if (result == "No errors found.") Color.Green else Color.Red)
            ) {
                Box(
                    modifier = Modifier
                        .background(if (result == "No errors found.") Color(0xFFDFF8D8) else Color(0xFFF8D8D8))
                        .padding(16.dp)
                ) {
                    Text(
                        text = result,
                        color = if (result == "No errors found.") Color.Green else Color.Red,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }


        Spacer(modifier = Modifier.height(8.dp))

        VisComponent(viewModel = viewModel)
    }
}

private fun parseInputArray(inputArrayStr: String): IntArray? {
    return try {
        inputArrayStr.split(",").map { it.trim().toInt() }.toIntArray()
    } catch (e: Exception) {
        null
    }
}