package com.example.archiveandroid.feature.home.chatbot.data.repository

import com.example.archiveandroid.feature.home.chatbot.data.model.ChatMessage
import kotlinx.coroutines.flow.Flow

interface ChatbotRepository {
    fun getMessages(): Flow<List<ChatMessage>>
    suspend fun sendMessage(message: String)
}

