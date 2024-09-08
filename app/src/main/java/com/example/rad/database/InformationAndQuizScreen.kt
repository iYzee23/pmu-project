package com.example.rad.database

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.rad.algorithm.AlgorithmViewModel
import com.example.rad.chatgpt.ChatViewModel
import com.example.rad.complexity.AlgorithmComponent
import com.example.rad.complexity.parseAlgorithmInfoJson
import com.example.rad.components.PythonComponent
import com.example.rad.quiz.Question
import com.example.rad.quiz.QuizComponent
import com.example.rad.quiz.QuizHistoryList
import com.example.rad.quiz.parseQuizJson
import com.example.rad.similarity.ComparisonComponent
import com.example.rad.similarity.parseAlgorithmComparisonsJson
import com.google.gson.Gson

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InformationAndQuizScreen(
    databaseViewModel: DatabaseViewModel,
    chatViewModel: ChatViewModel,
    viewModel: AlgorithmViewModel
) {
    var selectedAlgorithmName by remember { mutableStateOf("") }
    var algorithmNames by remember { mutableStateOf<List<String>>(emptyList()) }
    var isDropdownExpanded by remember { mutableStateOf(false) }
    var gResponse by remember { mutableStateOf("") }
    var gError by remember { mutableStateOf("") }
    var showQuizHistory by remember { mutableStateOf(false) }
    var quizHistoryList by remember { mutableStateOf<List<QuizHistory>>(emptyList()) }

    // Load algorithm names on launch
    LaunchedEffect(Unit) {
        databaseViewModel.getAllAlgorithmNames { names ->
            algorithmNames = names
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
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
                            // Fetch the algorithm code from the database when selected
                            databaseViewModel.getAlgorithm(name) { algorithm ->
                                viewModel.updateAlgorithmCode(algorithm?.code ?: "")
                            }

                            databaseViewModel.getQuizHistory(name) { history ->
                                quizHistoryList = history
                            }

                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { showQuizHistory = true }) {
            Text(text = "Show Quiz History")
        }

        if (showQuizHistory) {
            QuizHistoryList(quizHistoryList)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display Algorithm Code in PythonComponent (read-only)
        PythonComponent(
            viewModel = viewModel,
            inputArrayFix = "1, 2, 3, 4, 5",
            onClicked = {},
            isEditable = false
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Buttons for Quiz, Complexity, and Comparison
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = {
                chatViewModel.createChatCompletion(viewModel.algorithmCode.value, 0)  // Quiz request
            }) {
                Text(text = "Quiz")
            }

            Button(onClick = {
                chatViewModel.createChatCompletion(viewModel.algorithmCode.value, 1)  // Complexity request
            }) {
                Text(text = "Complexity")
            }

            Button(onClick = {
                chatViewModel.createChatCompletion(viewModel.algorithmCode.value, 2)  // Comparison request
            }) {
                Text(text = "Comparison")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // GPT Response Handling
        val chatGResponse by chatViewModel.gptResponse.observeAsState(initial = "")
        val chatGError by chatViewModel.gptError.observeAsState(initial = "")

        if (chatGResponse.isNotEmpty()) {
            gResponse = chatGResponse
            when (chatViewModel.currType.value) {
                0 -> {
                    val quiz = parseQuizJson(gResponse)
                    QuizComponent(
                        quiz = quiz,
                        algorithmName = selectedAlgorithmName,
                        databaseViewModel = databaseViewModel
                    )
                }
                1 -> {
                    val algorithmInfo = parseAlgorithmInfoJson(gResponse)
                    AlgorithmComponent(algorithmInfo = algorithmInfo)
                }
                2 -> {
                    val (algorithmComparisons, improvementSuggestions) = parseAlgorithmComparisonsJson(gResponse)
                    ComparisonComponent(
                        algorithmComparisons = algorithmComparisons,
                        improvementSuggestions = improvementSuggestions!!
                    )
                }
            }
        } else if (chatGError.isNotEmpty()) {
            gError = chatGError
            Text(text = gError, color = Color.Red)
        }
    }
}