package com.example.rad.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AlgorithmDao {
    @Query("SELECT name FROM algorithm_table")
    suspend fun getAllAlgorithmNames(): List<String>

    @Query("SELECT * FROM algorithm_table WHERE name = :name")
    suspend fun getAlgorithm(name: String): Algorithm?

    @Insert
    suspend fun insertAlgorithm(algorithm: Algorithm)

    @Query("DELETE FROM algorithm_table WHERE name = :name")
    suspend fun deleteAlgorithm(name: String)

    @Query("UPDATE algorithm_table SET code = :code WHERE name = :name")
    suspend fun updateAlgorithm(name: String, code: String)

    @Insert
    suspend fun insertQuizHistory(quizHistory: QuizHistory)

    @Query("SELECT * FROM quiz_history_table WHERE algorithmName = :algorithmName")
    suspend fun getQuizHistory(algorithmName: String): List<QuizHistory>
}
