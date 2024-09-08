package com.example.rad.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "algorithm_table")
data class Algorithm(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String = "",
    val code: String = ""
)
