package com.example.rad.database

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import com.example.rad.algorithm.AlgorithmViewModel
import com.example.rad.components.PythonComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreviewOrDeleteAlgorithm(
    databaseViewModel: DatabaseViewModel,
    viewModel: AlgorithmViewModel
) {
    var selectedAlgorithmName by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    var algorithmNames by remember { mutableStateOf<List<String>>(emptyList()) }
    var isDropdownExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.updateAlgorithmCode("")
    }

    // Load algorithm names on launch
    LaunchedEffect(Unit) {
        databaseViewModel.getAllAlgorithmNames { names ->
            algorithmNames = names
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        // Dropdown for selecting an algorithm to view or delete
        ExposedDropdownMenuBox(
            expanded = isDropdownExpanded,
            onExpandedChange = { isDropdownExpanded = !isDropdownExpanded }
        ) {
            OutlinedTextField(
                value = selectedAlgorithmName,
                onValueChange = {},
                label = { Text("Select Algorithm") },
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

        // Preview Algorithm Code in PythonComponent (read-only)
        PythonComponent(
            viewModel = viewModel,
            inputArrayFix = "1, 2, 3, 4, 5",
            onClicked = {
                databaseViewModel.deleteAlgorithm(selectedAlgorithmName) { names ->
                    result = "Algorithm has been successfully deleted."
                    viewModel.updateAlgorithmCode("")
                    algorithmNames = names // Update the dropdown with the new list
                }
            },
            isEditable = false,
            visibleButton = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Display result
        Text(text = "Result: $result")
    }
}
