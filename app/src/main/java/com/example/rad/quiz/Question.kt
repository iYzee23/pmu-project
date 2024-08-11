package com.example.rad.quiz

data class Question(
    val question: String,
    val answers: List<Answer>,
    val correctAnswers: List<Int>
)
