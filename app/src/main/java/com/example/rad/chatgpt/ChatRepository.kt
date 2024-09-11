package com.example.rad.chatgpt

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatRepository {

    private val apiClient = ApiClient.getInstance()

    fun createChatCompletion(message: String, callType: Int, chatViewModel: ChatViewModel) {
        try {
            val systemType = when (callType) {
                0 -> SYSTEM_QUIZ
                1 -> SYSTEM_COMPLEXITY
                else -> SYSTEM_SIMILARITY
            }
            val chatRequest = ChatRequest(
                arrayListOf(
                    Message(systemType, "system"),
                    Message(message, "user")
                ),
                CHAT_GPT_MODEL
            )

            apiClient.createChatCompletion(chatRequest).enqueue(object : Callback<ChatResponse> {
                override fun onResponse(call: Call<ChatResponse>, response: Response<ChatResponse>) {
                    val content = if (response.isSuccessful) {
                        chatViewModel.updateCurrType(callType)
                        chatViewModel.gptError.postValue("")
                        response.body()?.choices?.get(0)?.message?.content.orEmpty()
                    } else {
                        response.errorBody()?.string().orEmpty()
                    }

                    if (content.isNotEmpty()) {
                        chatViewModel.saveResponse(content, callType)
                    } else {
                        chatViewModel.gptError.postValue(content)
                    }
                    chatViewModel.setLoadingFinished()
                    chatViewModel.setGeneratingNewQuizFinished()
                }

                override fun onFailure(call: Call<ChatResponse>, t: Throwable) {
                    chatViewModel.gptError.postValue(t.message.orEmpty())
                    chatViewModel.setLoadingFinished()
                    chatViewModel.setGeneratingNewQuizFinished()
                }
            })
        } catch (e: Exception) {
            chatViewModel.gptError.postValue(e.message.orEmpty())
            chatViewModel.setLoadingFinished()
            chatViewModel.setGeneratingNewQuizFinished()
        }
    }
}

