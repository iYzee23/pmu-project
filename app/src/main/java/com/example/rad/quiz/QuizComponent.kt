package com.example.rad.quiz

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun QuizComponent(
    quiz: List<Question>
) {
    var quizCompleted by remember { mutableStateOf(false) }
    var score by remember { mutableStateOf(0) }

    if (quizCompleted) {
        QuizResult(
            score = score,
            totalQuestions = quiz.size,
            onClick = { quizCompleted = false }
        )
    } else {
        QuizMain(
            quiz = quiz,
            onClick = {
                quizCompleted = true
                score = it
            }
        )
    }
}

