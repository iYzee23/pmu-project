package com.example.rad.algorithm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AlgorithmViewModelProvider(
    private var algorithm: Algorithm
): ViewModelProvider.Factory {

    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        return AlgorithmViewModel(algorithm) as T
    }

}