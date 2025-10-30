package com.alpha.archive.feature.home.chatbot.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alpha.archive.feature.home.chatbot.data.model.ChatMessage
import com.alpha.archive.feature.home.chatbot.data.repository.ChatbotRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatbotViewModel @Inject constructor(
    private val repository: ChatbotRepository
) : ViewModel() {
    
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()
    
    private val _inputText = MutableStateFlow("")
    val inputText: StateFlow<String> = _inputText.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    init {
        loadMessages()
    }
    
    private fun loadMessages() {
        viewModelScope.launch {
            repository.getMessages().collect { messageList ->
                _messages.value = messageList
            }
        }
    }
    
    fun updateInputText(text: String) {
        _inputText.value = text
    }
    
    fun sendMessage() {
        val message = _inputText.value.trim()
        if (message.isNotBlank()) {
            _inputText.value = ""  // 즉시 입력창 비우기
            _isLoading.value = true  // 로딩 시작
            viewModelScope.launch {
                repository.sendMessage(message)
                _isLoading.value = false  // 로딩 종료
            }
        }
    }
    
    fun sendSuggestion(suggestionText: String) {
        _isLoading.value = true  // 로딩 시작
        viewModelScope.launch {
            repository.sendMessage(suggestionText)
            _isLoading.value = false  // 로딩 종료
        }
    }
}

