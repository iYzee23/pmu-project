package com.example.rad.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz_history_table")
data class QuizHistory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val algorithmName: String = "",
    val dateTaken: String = "",
    val score: Int = 0,
    val quizQuestions: String = "",
    val quizAnswers: String = ""
)
