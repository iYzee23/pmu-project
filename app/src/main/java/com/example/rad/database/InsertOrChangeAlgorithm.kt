package com.example.rad.database

import androidx.compose.foundation.background
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
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

    LaunchedEffect(Unit) {
        databaseViewModel.getAllAlgorithmNames { names ->
            algorithmNames = listOf("") + names
        }
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        ExposedDropdownMenuBox(
            expanded = isDropdownExpanded,
            onExpandedChange = { isDropdownExpanded = !isDropdownExpanded }
        ) {
            OutlinedTextField(
                value = selectedAlgorithmName,
                onValueChange = {},
                label = {
                    Text(
                        "Select or Insert Algorithm",
                        style = MaterialTheme.typography.labelLarge.copy(color = MaterialTheme.colorScheme.onBackground)
                    )
                },
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
                    .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.medium)
                    .padding(8.dp)
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
                            isNameFieldEnabled = name.isEmpty()
                            if (name.isNotEmpty()) {
                                databaseViewModel.getAlgorithm(name) { algorithm ->
                                    viewModel.updateAlgorithmCode(algorithm?.code ?: "")
                                }
                            } else {
                                viewModel.updateAlgorithmCode("")
                            }
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = selectedAlgorithmName,
            onValueChange = { selectedAlgorithmName = it },
            label = {
                Text("Algorithm Name", style = MaterialTheme.typography.labelLarge)
            },
            enabled = isNameFieldEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.medium)
                .padding(8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                SectionedHint(title = "Function Name", text = "Function must be named \"sort\" and have exactly one parameter \"arr\" that represents the array to be sorted or modified.")
                Spacer(modifier = Modifier.height(8.dp))
                SectionedHint(title = "Steps List", text = "Function must contain a list named \"steps\", which is the return value of the function.")
                Spacer(modifier = Modifier.height(8.dp))
                SectionedHint(title = "New Iterations", text = "In the \"steps\" list, each new iteration of the algorithm should be placed.")
            }
        }

        PythonComponent(
            viewModel = viewModel,
            inputArrayFix = "1, 2, 3, 4, 5",
            onClicked = { resultText ->
                result = resultText
                if (resultText == "No errors found.") {
                    if (isNameFieldEnabled && algorithmNames.contains(selectedAlgorithmName)) {
                        result = "Cannot insert algorithm with the name that already exists."
                    } else {
                        databaseViewModel.getAlgorithm(selectedAlgorithmName) { existingAlgorithm ->
                            if (existingAlgorithm != null) {
                                databaseViewModel.updateAlgorithm(selectedAlgorithmName, viewModel.algorithmCode.value) { names ->
                                    algorithmNames = listOf("") + names
                                    result = "Algorithm has been successfully updated."
                                }
                            } else {
                                val newAlgorithm = Algorithm(
                                    name = selectedAlgorithmName,
                                    code = viewModel.algorithmCode.value
                                )
                                databaseViewModel.insertAlgorithm(newAlgorithm) { names ->
                                    algorithmNames = listOf("") + names
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

        if (result.isNotEmpty()) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Result:",
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = MaterialTheme.colorScheme.secondary,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = result,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun SectionedHint(title: String, text: String) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall.copy(
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Bold
            )
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}
