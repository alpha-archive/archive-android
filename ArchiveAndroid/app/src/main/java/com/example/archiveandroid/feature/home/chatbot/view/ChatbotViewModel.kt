package com.example.archiveandroid.feature.home.chatbot.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.archiveandroid.feature.home.chatbot.data.model.ChatMessage
import com.example.archiveandroid.feature.home.chatbot.data.repository.ChatbotRepository
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
            viewModelScope.launch {
                repository.sendMessage(message)
            }
        }
    }
    
    fun sendSuggestion(suggestionText: String) {
        viewModelScope.launch {
            repository.sendMessage(suggestionText)
        }
    }
}

