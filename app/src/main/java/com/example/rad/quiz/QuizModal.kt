package com.example.rad.quiz

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun QuizModal(
    quiz: List<Question>,
    quizCompleted: Boolean,
    score: Int,
    onClose: () -> Unit,
    onQuizComplete: (Int, Map<Int, List<Int>>) -> Unit
) {
    Dialog(onDismissRequest = onClose) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            shadowElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                if (quizCompleted) {
                    QuizResult(
                        score = score,
                        totalQuestions = quiz.size,
                        onClick = onClose
                    )
                } else {
                    QuizMain(
                        quiz = quiz,
                        onQuizComplete = onQuizComplete
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onClose,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(text = "Close")
                }
            }
        }
    }
}