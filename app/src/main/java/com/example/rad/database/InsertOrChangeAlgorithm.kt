package com.example.rad.database

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.example.rad.components.PythonComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsertOrChangeAlgorithm(
    databaseViewModel: DatabaseViewModel,
    viewModel: AlgorithmViewModel
) {
    var selectedAlgorithmName by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    var algorithmNames by remember { mutableStateOf<List<String>>(emptyList()) }
    var isDropdownExpanded by remember { mutableStateOf(false) }
    var isNameFieldEnabled by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.updateAlgorithmCode("")
    }

    // Load all algorithm names
    LaunchedEffect(Unit) {
        databaseViewModel.getAllAlgorithmNames { names ->
            algorithmNames = listOf("") + names // Add empty option for new algorithm
        }
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Dropdown to select or insert algorithm
        ExposedDropdownMenuBox(
            expanded = isDropdownExpanded,
            onExpandedChange = { isDropdownExpanded = !isDropdownExpanded }
        ) {
            OutlinedTextField(
                value = selectedAlgorithmName,
                onValueChange = {},
                label = { Text("Select or Insert Algorithm") },
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = isDropdownExpanded,
                onDismissRequest = { isDropdownExpanded = false }
            ) {
                algorithmNames.forEach { name ->
                    DropdownMenuItem(
                        text = { Text(if (name.isEmpty()) "New Algorithm" else name) },
                        onClick = {
                            selectedAlgorithmName = name
                            isDropdownExpanded = false
                            isNameFieldEnabled = name.isEmpty() // Enable text field for new algorithm
                            if (name.isNotEmpty()) {
                                databaseViewModel.getAlgorithm(name) { algorithm ->
                                    viewModel.updateAlgorithmCode(algorithm?.code ?: "")
                                }
                            } else {
                                viewModel.updateAlgorithmCode("") // Clear for new algorithm
                            }
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Algorithm name input (enabled only for new algorithms)
        OutlinedTextField(
            value = selectedAlgorithmName,
            onValueChange = { selectedAlgorithmName = it },
            label = { Text("Algorithm Name") },
            enabled = isNameFieldEnabled,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Code writing hint for the user
        Text(
            text = """
                - Function must be named "sort" and have exactly one parameter "arr" that represents the array to be sorted or modified.
                - Function must contain a list named "steps", which is the return value of the function.
                - In the "steps" list, each new iteration of the algorithm should be placed.
            """.trimIndent(),
            style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.primary),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // PythonComponent for algorithm code input
        PythonComponent(
            viewModel = viewModel,
            inputArrayFix = "1, 2, 3, 4, 5",
            onClicked = { resultText ->
                result = resultText
                if (resultText == "No errors found.") {
                    // If creating a new algorithm, check if the name already exists
                    if (isNameFieldEnabled && algorithmNames.contains(selectedAlgorithmName)) {
                        result = "Cannot insert algorithm with the name that already exists."
                    } else {
                        // Check if the algorithm exists and then either update or insert it
                        databaseViewModel.getAlgorithm(selectedAlgorithmName) { existingAlgorithm ->
                            if (existingAlgorithm != null) {
                                // Algorithm exists, update it
                                databaseViewModel.updateAlgorithm(selectedAlgorithmName, viewModel.algorithmCode.value) { names ->
                                    algorithmNames = listOf("") + names // Update the dropdown with the new list
                                    result = "Algorithm has been successfully updated."
                                }
                            } else {
                                // Algorithm doesn't exist, insert new one
                                val newAlgorithm = Algorithm(
                                    name = selectedAlgorithmName,
                                    code = viewModel.algorithmCode.value
                                )
                                databaseViewModel.insertAlgorithm(newAlgorithm) { names ->
                                    algorithmNames = listOf("") + names // Update the dropdown with the new list
                                    result = "Algorithm has been successfully inserted."
                                }
                            }
                        }
                    }
                }
            },
            isEditable = true,
            visibleButton = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Display result
        Text(text = "Result: $result")
    }
}
