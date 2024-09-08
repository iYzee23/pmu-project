package com.example.rad.quiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.rad.database.DatabaseViewModel
import com.example.rad.database.QuizHistory
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun QuizComponent(
    quiz: List<Question>,
    algorithmName: String,  // Add the algorithm name to save quiz history
    databaseViewModel: DatabaseViewModel  // Pass the viewModel to save quiz results
) {
    var showQuizModal by remember { mutableStateOf(false) }
    var quizCompleted by remember { mutableStateOf(false) }
    var score by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Button to open the Quiz Modal
        Button(onClick = { showQuizModal = true }) {
            Text(text = if (quizCompleted) "Show Quiz Result" else "Start Quiz")
        }

        // Show modal for the quiz
        if (showQuizModal) {
            QuizModal(
                quiz = quiz,
                quizCompleted = quizCompleted,
                score = score,
                onClose = { showQuizModal = false },
                onQuizComplete = { completedScore, selectedAnswersMap ->
                    quizCompleted = true
                    score = completedScore

                    // Serialize the quiz questions and answers
                    val quizQuestionsJson = Gson().toJson(quiz)
                    val quizAnswersJson = Gson().toJson(selectedAnswersMap)

                    // Save the quiz history to the database
                    val dateTaken = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
                    val quizHistory = QuizHistory(
                        algorithmName = algorithmName,
                        dateTaken = dateTaken,
                        score = score,
                        quizQuestions = quizQuestionsJson,
                        quizAnswers = quizAnswersJson
                    )
                    databaseViewModel.insertQuizHistory(quizHistory)
                }
            )
        }
    }
}

