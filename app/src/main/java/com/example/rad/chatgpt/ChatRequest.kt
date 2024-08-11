package com.example.rad.chatgpt

data class ChatRequest(
    val messages: List<Message>,
    val model: String
)


