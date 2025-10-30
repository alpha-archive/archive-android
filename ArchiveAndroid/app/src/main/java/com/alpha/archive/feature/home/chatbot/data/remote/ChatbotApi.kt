package com.alpha.archive.feature.home.chatbot.data.remote

import com.alpha.archive.core.network.dto.ApiResponse
import com.alpha.archive.feature.home.chatbot.data.remote.dto.ChatbotMessageRequest
import com.alpha.archive.feature.home.chatbot.data.remote.dto.ChatbotMessageResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ChatbotApi {
    @POST("api/chatbot/message")
    suspend fun sendMessage(
        @Body request: ChatbotMessageRequest
    ): Response<ApiResponse<ChatbotMessageResponse>>
}

