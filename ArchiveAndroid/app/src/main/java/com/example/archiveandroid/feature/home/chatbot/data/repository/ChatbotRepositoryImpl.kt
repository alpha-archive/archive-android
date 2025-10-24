package com.example.archiveandroid.feature.home.chatbot.data.repository

import androidx.compose.ui.graphics.Color
import com.example.archiveandroid.feature.home.chatbot.data.model.ChatMessage
import com.example.archiveandroid.feature.home.chatbot.data.model.MessageType
import com.example.archiveandroid.feature.home.chatbot.data.model.SuggestionChip
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class ChatbotRepositoryImpl @Inject constructor() : ChatbotRepository {
    
    private val _messages = MutableStateFlow(getInitialMessages())
    
    override fun getMessages(): Flow<List<ChatMessage>> = _messages.asStateFlow()
    
    override suspend fun sendMessage(message: String) {
        val newMessage = ChatMessage(
            id = _messages.value.size + 1,
            type = MessageType.USER,
            content = message
        )
        _messages.value = _messages.value + newMessage
        
        // TODO: 실제 API 호출로 대체
        // 임시 봇 응답
        val botResponse = ChatMessage(
            id = _messages.value.size + 1,
            type = MessageType.BOT,
            content = "메시지를 받았습니다: $message"
        )
        _messages.value = _messages.value + botResponse
    }
    
    private fun getInitialMessages(): List<ChatMessage> {
        return listOf(
            ChatMessage(
                id = 1,
                type = MessageType.BOT,
                content = "안녕하세요! 어떤 오프라인 활동을 기록하고 싶으신가요?"
            ),
            ChatMessage(
                id = 2,
                type = MessageType.SUGGESTION,
                content = "",
                suggestions = listOf(
                    SuggestionChip("🏃 스포츠관람", "🏃", Color(0xFF4CAF50)),
                    SuggestionChip("✈️ 여행", "✈️", Color(0xFF2196F3)),
                    SuggestionChip("🏛️ 체험", "🏛️", Color(0xFFFFC107))
                )
            )
        )
    }
}

