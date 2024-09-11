package com.example.rad.database

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
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
    var inputError by remember { mutableStateOf("") }
    var algorithmError by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        databaseViewModel.getAllAlgorithmNames { names ->
            algorithmNames = names
            viewModel.resetVisualizer()
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Column {
            OutlinedTextField(
                value = inputArray,
                onValueChange = {
                    inputArray = it
                    inputError = ""
                },
                label = { Text("Input Array (CSV format)") },
                isError = inputError.isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 100.dp)
                    .verticalScroll(rememberScrollState()),
                maxLines = 3,
                singleLine = false
            )
            if (inputError.isNotEmpty()) {
                Text(
                    text = inputError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column {
            ExposedDropdownMenuBox(
                expanded = isDropdownExpanded,
                onExpandedChange = { isDropdownExpanded = !isDropdownExpanded }
            ) {
                OutlinedTextField(
                    value = selectedAlgorithmName,
                    onValueChange = {},
                    label = { Text("Select Algorithm") },
                    isError = algorithmError.isNotEmpty(),
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
                                algorithmError = ""
                                databaseViewModel.getAlgorithm(name) { algorithm ->
                                    viewModel.updateAlgorithmCode(algorithm?.code ?: "")
                                }
                            }
                        )
                    }
                }
            }
            if (algorithmError.isNotEmpty()) {
                Text(
                    text = algorithmError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val inputArrayParsed = parseInputArray(inputArray)
                when {
                    selectedAlgorithmName.isEmpty() -> algorithmError = "You must select an algorithm."
                    inputArrayParsed == null -> inputError = "Invalid input array format."
                    else -> {
                        inputError = ""
                        algorithmError = ""
                        viewModel.runPythonCode(viewModel.algorithmCode.value, inputArrayParsed) {}
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Load Algorithm and Run")
        }

        Spacer(modifier = Modifier.height(16.dp))

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
