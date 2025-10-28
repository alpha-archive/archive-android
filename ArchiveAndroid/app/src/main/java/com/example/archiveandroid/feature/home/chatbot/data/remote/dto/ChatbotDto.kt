package com.example.archiveandroid.feature.home.chatbot.data.remote.dto

/**
 * 챗봇 메시지 요청 DTO
 */
data class ChatbotMessageRequest(
    val message: String
)

/**
 * 챗봇 메시지 응답 DTO
 */
data class ChatbotMessageResponse(
    val reply: String
)

