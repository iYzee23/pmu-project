package com.example.rad.chatgpt

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class ChatViewModel(application: Application): AndroidViewModel(application) {

    private val chatRepository = ChatRepository()

    val gptResponse: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val gptError: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val currType: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }

    fun createChatCompletion(message: String, callType: Int) {
        chatRepository.createChatCompletion(message, callType, this)
    }

    fun updateCurrType(newType: Int) {
        currType.value = newType
    }
}