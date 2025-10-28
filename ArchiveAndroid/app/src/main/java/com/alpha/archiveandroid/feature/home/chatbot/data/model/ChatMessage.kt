package com.alpha.archiveandroid.feature.home.chatbot.data.model

import androidx.compose.ui.graphics.Color

// 메시지 타입
enum class MessageType {
    BOT, USER, SUGGESTION
}

// 메시지 데이터 클래스
data class ChatMessage(
    val id: Int,
    val type: MessageType,
    val content: String,
    val suggestions: List<SuggestionChip> = emptyList()
)

// 제안 칩 데이터 클래스
data class SuggestionChip(
    val text: String,
    val icon: String,
    val color: Color
)

