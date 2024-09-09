package com.example.rad.chatgpt

import android.util.Log
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
                    Message(
                        systemType,
                        "system"
                    ),
                    Message(
                        message,
                        "user"
                    )
                ),
                CHAT_GPT_MODEL
            )
            apiClient.createChatCompletion(chatRequest).enqueue(object: Callback<ChatResponse> {

                override fun onResponse(
                    call: Call<ChatResponse>,
                    response: Response<ChatResponse>
                ) {
                    val code = response.code()
                    var content = ""
                    if (code == 200) {
                        response.body()?.choices?.get(0)?.message?.let {
                            content = it.content
                            Log.d("GPT_Message", content)
                            chatViewModel.gptResponse.postValue(content)
                            chatViewModel.gptError.postValue("")

                            chatViewModel.updateCurrType(callType)
                        }
                    }
                    else {
                        content = response.errorBody()?.string() ?: "Unknown error"
                        Log.d("GPT_Error", content)
                        chatViewModel.gptResponse.postValue("")
                        chatViewModel.gptError.postValue(content)
                    }
                }

                override fun onFailure(
                    call: Call<ChatResponse>,
                    t: Throwable
                ) {
                    t.printStackTrace()
                    val content = t.message ?: "Network request failed"
                    Log.d("GPT_Error", content)
                    chatViewModel.gptResponse.postValue("")
                    chatViewModel.gptError.postValue(content)
                }

            })
        }
        catch (e: Exception) {
            e.printStackTrace()
            val content = e.message ?: "Exception occurred"
            Log.d("GPT_Error", content)
            chatViewModel.gptResponse.postValue("")
            chatViewModel.gptError.postValue(content)
        }
    }

}