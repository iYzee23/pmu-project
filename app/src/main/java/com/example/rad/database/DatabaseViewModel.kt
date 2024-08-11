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

    fun insertAlgorithm(algorithm: Algorithm) {
        viewModelScope.launch {
            algorithmDao.insertAlgorithm(algorithm)
        }
    }

    fun deleteAlgorithm(name: String) {
        viewModelScope.launch {
            algorithmDao.deleteAlgorithm(name)
        }
    }
}