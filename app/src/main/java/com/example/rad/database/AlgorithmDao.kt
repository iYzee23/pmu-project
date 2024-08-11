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
}
