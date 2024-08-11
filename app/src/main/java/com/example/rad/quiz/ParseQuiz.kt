package com.example.rad.quiz

import kotlinx.serialization.*
import kotlinx.serialization.json.*

@Serializable
data class RawAnswer(
    val id: Int,
    val text: String
)

@Serializable
data class RawQuestion(
    val question: String,
    val answers: List<Map<Int, String>>,
    val correct_answers: List<Int>
)

fun parseQuizJson(input: String): List<Question> {
    val json = Json { ignoreUnknownKeys = true }
    val rawQuestions = json.decodeFromString<List<RawQuestion>>(input)

    return rawQuestions.map { rawQuestion ->
        val answers = rawQuestion.answers.map { entry ->
            val (id, text) = entry.entries.first()
            Answer(id, text)
        }
        Question(
            question = rawQuestion.question,
            answers = answers,
            correctAnswers = rawQuestion.correct_answers
        )
    }
}
