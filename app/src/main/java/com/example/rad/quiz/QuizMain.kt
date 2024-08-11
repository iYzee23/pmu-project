package com.example.rad.quiz

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun QuizMain(
    quiz: List<Question>,
    onClick: (Int) -> Unit
) {
    var currentQuestionIndex by remember { mutableStateOf(0) }
    val selectedAnswersMap = remember { mutableStateMapOf<Int, List<Int>>() }

    val currentQuestion = quiz[currentQuestionIndex]

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = currentQuestion.question,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Retrieve the selected answers for the current question
        val selectedAnswers = selectedAnswersMap[currentQuestionIndex] ?: emptyList()

        currentQuestion.answers.forEach { answer ->
            val isSelected = selectedAnswers.contains(answer.id)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        val updatedAnswers = selectedAnswers.toMutableList().apply {
                            if (isSelected) remove(answer.id) else add(answer.id)
                        }
                        // Update the map with a new list to trigger recomposition
                        selectedAnswersMap[currentQuestionIndex] = updatedAnswers
                    }
                    .padding(8.dp)
            ) {
                Checkbox(
                    checked = isSelected,
                    onCheckedChange = null // Checkbox is handled by Row click
                )
                Text(
                    text = answer.text,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    if (currentQuestionIndex > 0) {
                        currentQuestionIndex--
                    }
                },
                enabled = currentQuestionIndex > 0
            ) {
                Text(text = "Back")
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (currentQuestionIndex < quiz.size - 1) {
                        currentQuestionIndex++
                    } else {
                        val score = calculateScore(quiz, selectedAnswersMap)
                        onClick(score)
                    }
                }
            ) {
                Text(text = if (currentQuestionIndex < quiz.size - 1) "Next" else "Finish")
            }
        }
    }
}

fun calculateScore(quiz: List<Question>, selectedAnswersMap: Map<Int, List<Int>>): Int {
    var score = 0

    quiz.forEachIndexed { index, question ->
        val selectedAnswers = selectedAnswersMap[index] ?: emptyList()
        if (selectedAnswers.sorted() == question.correctAnswers.sorted()) {
            score++
        }
    }

    return score
}
