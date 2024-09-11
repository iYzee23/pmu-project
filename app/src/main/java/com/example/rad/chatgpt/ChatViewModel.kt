package com.example.rad.chatgpt

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class ChatViewModel(application: Application) : AndroidViewModel(application) {

    private val chatRepository = ChatRepository()

    val isGeneratingNewQuiz: MutableLiveData<Boolean> by lazy { MutableLiveData(false) }
    val isLoading: MutableLiveData<Boolean> by lazy { MutableLiveData(false) }

    val quizResponse: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val complexityResponse: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val comparisonResponse: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    val gptError: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val currType: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }

    val isLoadingQuiz: MutableLiveData<Boolean> by lazy { MutableLiveData(false) }
    val isLoadingComplexity: MutableLiveData<Boolean> by lazy { MutableLiveData(false) }
    val isLoadingComparison: MutableLiveData<Boolean> by lazy { MutableLiveData(false) }

    fun createChatCompletion(message: String, callType: Int) {
        isLoading.value = true
        when (callType) {
            0 -> {
                isGeneratingNewQuiz.value = true
                isLoadingQuiz.value = true
            }
            1 -> isLoadingComplexity.value = true
            2 -> isLoadingComparison.value = true
        }

        chatRepository.createChatCompletion(message, callType, this)
    }

    fun saveResponse(response: String, callType: Int) {
        when (callType) {
            0 -> quizResponse.value = response
            1 -> complexityResponse.value = response
            2 -> comparisonResponse.value = response
        }
        isLoadingQuiz.value = false
        isLoadingComplexity.value = false
        isLoadingComparison.value = false
    }

    fun clearResponses() {
        quizResponse.value = ""
        complexityResponse.value = ""
        comparisonResponse.value = ""
    }

    fun clearQuizResponse() {
        quizResponse.value = ""
    }

    fun setLoadingFinished() {
        isLoading.value = false
    }

    fun setGeneratingNewQuizFinished() {
        isGeneratingNewQuiz.value = false
    }

    fun updateCurrType(newType: Int) {
        currType.value = newType
    }
}
