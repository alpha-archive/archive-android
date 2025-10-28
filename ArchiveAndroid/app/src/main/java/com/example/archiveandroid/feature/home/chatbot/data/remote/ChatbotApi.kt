package com.example.archiveandroid.feature.home.chatbot.data.remote

import com.example.archiveandroid.core.network.dto.ApiResponse
import com.example.archiveandroid.feature.home.chatbot.data.remote.dto.ChatbotMessageRequest
import com.example.archiveandroid.feature.home.chatbot.data.remote.dto.ChatbotMessageResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ChatbotApi {
    @POST("api/chatbot/message")
    suspend fun sendMessage(
        @Body request: ChatbotMessageRequest
    ): Response<ApiResponse<ChatbotMessageResponse>>
}

