package com.example.rad.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class DatabaseViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val algorithmDao: AlgorithmDao = AlgorithmDatabase
        .getDatabase(application, viewModelScope)
        .algorithmDao()

    fun getAllAlgorithmNames(callback: (List<String>) -> Unit) {
        viewModelScope.launch {
            val names = algorithmDao.getAllAlgorithmNames()
            callback(names)
        }
    }

    fun getAlgorithm(name: String, callback: (Algorithm?) -> Unit) {
        viewModelScope.launch {
            val algorithm = algorithmDao.getAlgorithm(name)
            callback(algorithm)
        }
    }

    fun insertAlgorithm(algorithm: Algorithm, callback: (List<String>) -> Unit) {
        viewModelScope.launch {
            algorithmDao.insertAlgorithm(algorithm)
            val names = algorithmDao.getAllAlgorithmNames()
            callback(names)
        }
    }

    fun deleteAlgorithm(name: String, callback: (List<String>) -> Unit) {
        viewModelScope.launch {
            algorithmDao.deleteAlgorithm(name)
            val names = algorithmDao.getAllAlgorithmNames()
            callback(names)
        }
    }

    fun updateAlgorithm(name: String, code: String, callback: (List<String>) -> Unit) {
        viewModelScope.launch {
            algorithmDao.updateAlgorithm(name, code)
            val names = algorithmDao.getAllAlgorithmNames()
            callback(names)
        }
    }

    fun insertQuizHistory(quizHistory: QuizHistory) {
        viewModelScope.launch {
            algorithmDao.insertQuizHistory(quizHistory)
        }
    }

    fun getQuizHistory(algorithmName: String, callback: (List<QuizHistory>) -> Unit) {
        viewModelScope.launch {
            val history = algorithmDao.getQuizHistory(algorithmName)
            callback(history)
        }
    }
}