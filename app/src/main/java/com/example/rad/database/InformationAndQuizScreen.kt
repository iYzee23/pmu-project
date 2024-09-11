package com.example.rad.database

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.rad.algorithm.AlgorithmViewModel
import com.example.rad.chatgpt.ChatViewModel
import com.example.rad.complexity.AlgorithmComponent
import com.example.rad.complexity.parseAlgorithmInfoJson
import com.example.rad.components.PythonComponent
import com.example.rad.quiz.QuizComponent
import com.example.rad.quiz.QuizHistoryList
import com.example.rad.quiz.parseQuizJson
import com.example.rad.similarity.ComparisonComponent
import com.example.rad.similarity.parseAlgorithmComparisonsJson

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
    var showQuizHistory by remember { mutableStateOf(false) }
    var quizHistoryList by remember { mutableStateOf<List<QuizHistory>>(emptyList()) }

    val quizResponse by chatViewModel.quizResponse.observeAsState("")
    val complexityResponse by chatViewModel.complexityResponse.observeAsState("")
    val comparisonResponse by chatViewModel.comparisonResponse.observeAsState("")

    val isLoading by chatViewModel.isLoading.observeAsState(false)
    val isLoadingQuiz by chatViewModel.isLoadingQuiz.observeAsState(false)
    val isLoadingComplexity by chatViewModel.isLoadingComplexity.observeAsState(false)
    val isLoadingComparison by chatViewModel.isLoadingComparison.observeAsState(false)
    val isGeneratingNewQuiz by chatViewModel.isGeneratingNewQuiz.observeAsState(false)

    LaunchedEffect(Unit) {
        viewModel.updateAlgorithmCode("")
        chatViewModel.clearResponses()
        chatViewModel.updateCurrType(-1)
    }

    LaunchedEffect(Unit) {
        databaseViewModel.getAllAlgorithmNames { names ->
            algorithmNames = names
        }
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .then(if (!showQuizHistory) Modifier.verticalScroll(rememberScrollState()) else Modifier)
            .background(MaterialTheme.colorScheme.background)
    ) {
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
                    .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.medium)
                    .padding(8.dp)
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
                            databaseViewModel.getQuizHistory(name) { history ->
                                quizHistoryList = history
                            }
                        }
                    )
                }
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().height(80.dp)
        ) {
            if (!showQuizHistory) {
                Button(
                    onClick = { showQuizHistory = true },
                    enabled = selectedAlgorithmName.isNotEmpty(),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Show History")
                }
                /*
                else if (chatGError.isNotEmpty()) {
                    gError = chatGError
                    Text(text = gError, color = Color.Red)
                }
                */
            } else {
                Button(
                    onClick = { showQuizHistory = false },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Show Info Page")
                }
            }
        }

        if (showQuizHistory) {
            QuizHistoryList(quizHistoryList)
        }
        else {
            PythonComponent(
                viewModel = viewModel,
                inputArrayFix = "1, 2, 3, 4, 5",
                onClicked = {},
                isEditable = false,
                visibleButton = false
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (quizResponse.isEmpty()) {
                    Button(
                        onClick = {
                            chatViewModel.createChatCompletion(viewModel.algorithmCode.value, 0)
                        },
                        enabled = selectedAlgorithmName.isNotEmpty() && !isLoading,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (isLoadingQuiz) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.secondary
                            )
                        } else {
                            Text(text = "Generate quiz")
                        }
                    }
                } else {
                    val quiz = parseQuizJson(quizResponse)
                    QuizComponent(
                        quiz = quiz,
                        algorithmName = selectedAlgorithmName,
                        databaseViewModel = databaseViewModel,
                        onGenerateNewQuiz = {
                            chatViewModel.clearQuizResponse()
                            chatViewModel.createChatCompletion(viewModel.algorithmCode.value, 0)
                        },
                        chatViewModel = chatViewModel
                    )
                }

                if (complexityResponse.isEmpty()) {
                    Button(
                        onClick = {
                            chatViewModel.createChatCompletion(viewModel.algorithmCode.value, 1)
                        },
                        enabled = selectedAlgorithmName.isNotEmpty() && !isLoading,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (isLoadingComplexity) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.secondary
                            )
                        } else {
                            Text(text = "Calculate Complexity")
                        }
                    }
                } else {
                    val algorithmInfo = parseAlgorithmInfoJson(complexityResponse)
                    AlgorithmComponent(algorithmInfo = algorithmInfo)
                }

                if (comparisonResponse.isEmpty()) {
                    Button(
                        onClick = {
                            chatViewModel.createChatCompletion(viewModel.algorithmCode.value, 2)
                        },
                        enabled = selectedAlgorithmName.isNotEmpty() && !isLoading,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (isLoadingComparison) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.secondary
                            )
                        } else {
                            Text(text = "Get Comparisons")
                        }
                    }
                } else {
                    val (algorithmComparisons, improvementSuggestions) = parseAlgorithmComparisonsJson(comparisonResponse)
                    ComparisonComponent(
                        algorithmComparisons = algorithmComparisons,
                        improvementSuggestions = improvementSuggestions!!
                    )
                }
            }
        }
    }
}
