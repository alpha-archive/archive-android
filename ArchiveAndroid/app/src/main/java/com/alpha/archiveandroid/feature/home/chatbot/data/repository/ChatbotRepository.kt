package com.alpha.archiveandroid.feature.home.chatbot.data.repository

import com.alpha.archiveandroid.feature.home.chatbot.data.model.ChatMessage
import kotlinx.coroutines.flow.Flow

interface ChatbotRepository {
    fun getMessages(): Flow<List<ChatMessage>>
    suspend fun sendMessage(message: String)
}

