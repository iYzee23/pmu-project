package com.example.rad.database

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatabaseComponent(
    databaseViewModel: DatabaseViewModel
) {
    // State variables
    var algorithmCode by remember { mutableStateOf<String?>(null) }

    var insertAlgorithmName by remember { mutableStateOf("") }
    var insertAlgorithmCode by remember { mutableStateOf("") }

    var selectedAlgorithmNameForDeletion by remember { mutableStateOf("") }
    var selectedAlgorithmNameForViewing by remember { mutableStateOf("") }

    var algorithmNames by remember { mutableStateOf<List<String>>(emptyList()) }
    var isDropdownExpandedForDeletion by remember { mutableStateOf(false) }
    var isDropdownExpandedForViewing by remember { mutableStateOf(false) }

    // Load all algorithm names from the database
    LaunchedEffect(Unit) {
        databaseViewModel.getAllAlgorithmNames { names ->
            algorithmNames = names
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Algorithm Manager") })
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Section 1: Algorithm Insertion
            DatabaseSection(title = "Insert New Algorithm")
            OutlinedTextField(
                value = insertAlgorithmName,
                onValueChange = { insertAlgorithmName = it },
                label = { Text("Algorithm Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = insertAlgorithmCode,
                onValueChange = { insertAlgorithmCode = it },
                label = { Text("Algorithm Code") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                maxLines = 10
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    val algo = Algorithm(
                        name = insertAlgorithmName,
                        code = insertAlgorithmCode
                    )
                    databaseViewModel.insertAlgorithm(algo)
                    insertAlgorithmName = ""
                    insertAlgorithmCode = ""
                    databaseViewModel.getAllAlgorithmNames { names ->
                        algorithmNames = names
                    }
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Insert Algorithm")
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Section 2: Algorithm Deletion with Dropdown
            DatabaseSection(title = "Delete Algorithm")
            ExposedDropdownMenuBox(
                expanded = isDropdownExpandedForDeletion,
                onExpandedChange = { isDropdownExpandedForDeletion = !isDropdownExpandedForDeletion }
            ) {
                OutlinedTextField(
                    value = selectedAlgorithmNameForDeletion,
                    onValueChange = {},
                    label = { Text("Select Algorithm to Delete") },
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = isDropdownExpandedForDeletion,
                    onDismissRequest = { isDropdownExpandedForDeletion = false }
                ) {
                    algorithmNames.forEach { name ->
                        DropdownMenuItem(
                            text = { Text(name) },
                            onClick = {
                                selectedAlgorithmNameForDeletion = name
                                isDropdownExpandedForDeletion = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    databaseViewModel.deleteAlgorithm(selectedAlgorithmNameForDeletion)
                    selectedAlgorithmNameForDeletion = ""
                    databaseViewModel.getAllAlgorithmNames { names ->
                        algorithmNames = names
                    }
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Delete Algorithm")
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Section 3: Algorithm Viewer with Dropdown
            DatabaseSection(title = "View Algorithm Code")
            ExposedDropdownMenuBox(
                expanded = isDropdownExpandedForViewing,
                onExpandedChange = { isDropdownExpandedForViewing = !isDropdownExpandedForViewing }
            ) {
                OutlinedTextField(
                    value = selectedAlgorithmNameForViewing,
                    onValueChange = {},
                    label = { Text("Select Algorithm to View") },
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = isDropdownExpandedForViewing,
                    onDismissRequest = { isDropdownExpandedForViewing = false }
                ) {
                    algorithmNames.forEach { name ->
                        DropdownMenuItem(
                            text = { Text(name) },
                            onClick = {
                                selectedAlgorithmNameForViewing = name
                                isDropdownExpandedForViewing = false
                                databaseViewModel.getAlgorithm(selectedAlgorithmNameForViewing) { algorithm ->
                                    algorithmCode = algorithm?.code
                                }
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Surface {
                if (algorithmCode != null) {
                    Text(text = algorithmCode ?: "No code found")
                } else {
                    Text(text = "Algorithm not found")
                }
            }
        }
    }
}