package com.example.rad.quiz

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.rad.chatgpt.ChatViewModel
import com.example.rad.database.DatabaseViewModel
import com.example.rad.database.QuizHistory
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun QuizComponent(
    quiz: List<Question>,
    algorithmName: String,
    databaseViewModel: DatabaseViewModel,
    chatViewModel: ChatViewModel,
    onGenerateNewQuiz: () -> Unit,
) {
    var showQuizModal by remember { mutableStateOf(false) }
    var quizCompleted by remember { mutableStateOf(false) }
    var score by remember { mutableStateOf(0) }

    val isLoading by chatViewModel.isLoading.observeAsState(false)

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(
            modifier = Modifier
                .weight(0.85f)
                .height(48.dp),
            onClick = { showQuizModal = true },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiary
            )
        ) {
            Text(text = if (quizCompleted) "Show Quiz Result" else "Start Quiz")
        }

        Spacer(modifier = Modifier.width(4.dp))

        IconButton(
            modifier = Modifier
                .size(48.dp)
                .background(
                    if (!isLoading) MaterialTheme.colorScheme.tertiary else Color.LightGray,
                    shape = CircleShape
                ),
            onClick = { onGenerateNewQuiz() },
            enabled = !isLoading
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Generate New Quiz",
                tint = MaterialTheme.colorScheme.onTertiary
            )
        }
    }

    if (showQuizModal) {
        QuizModal(
            quiz = quiz,
            quizCompleted = quizCompleted,
            score = score,
            onClose = { showQuizModal = false },
            onQuizComplete = { completedScore, selectedAnswersMap ->
                quizCompleted = true
                score = completedScore

                val quizQuestionsJson = Gson().toJson(quiz)
                val quizAnswersJson = Gson().toJson(selectedAnswersMap)

                val dateTaken = SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss",
                    Locale.getDefault()).format(Date()
                )
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

