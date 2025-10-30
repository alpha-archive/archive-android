package com.alpha.archive.feature.home.chatbot.data.repository

import android.util.Log
import androidx.compose.ui.graphics.Color
import com.alpha.archive.core.repository.BaseRepository
import com.alpha.archive.feature.home.chatbot.data.model.ChatMessage
import com.alpha.archive.feature.home.chatbot.data.model.MessageType
import com.alpha.archive.feature.home.chatbot.data.model.SuggestionChip
import com.alpha.archive.feature.home.chatbot.data.remote.ChatbotApi
import com.alpha.archive.feature.home.chatbot.data.remote.dto.ChatbotMessageRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class ChatbotRepositoryImpl @Inject constructor(
    private val chatbotApi: ChatbotApi
) : ChatbotRepository, BaseRepository() {
    
    private val _messages = MutableStateFlow(getInitialMessages())
    
    override fun getMessages(): Flow<List<ChatMessage>> = _messages.asStateFlow()
    
    override suspend fun sendMessage(message: String) {
        // 사용자 메시지 추가
        val newMessage = ChatMessage(
            id = _messages.value.size + 1,
            type = MessageType.USER,
            content = message
        )
        _messages.value = _messages.value + newMessage
        
        // API 호출
        val request = ChatbotMessageRequest(message = message)
        val result = handleApiCall { chatbotApi.sendMessage(request) }
        
        result.onSuccess { response ->
            val botResponse = ChatMessage(
                id = _messages.value.size + 1,
                type = MessageType.BOT,
                content = response.reply
            )
            _messages.value = _messages.value + botResponse
        }
        
        result.onFailure { error ->
            Log.e("ChatbotRepository", "API 호출 실패: ${error.message}")
            // 에러 시 기본 응답
            val errorResponse = ChatMessage(
                id = _messages.value.size + 1,
                type = MessageType.BOT,
                content = "죄송합니다. 일시적인 오류가 발생했습니다. 다시 시도해주세요."
            )
            _messages.value = _messages.value + errorResponse
        }
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

