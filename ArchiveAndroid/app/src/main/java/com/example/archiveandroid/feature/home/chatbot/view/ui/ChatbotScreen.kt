package com.example.archiveandroid.feature.home.chatbot.view.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.archiveandroid.feature.home.chatbot.data.model.MessageType
import com.example.archiveandroid.feature.home.chatbot.view.ChatbotViewModel
import com.example.archiveandroid.feature.home.chatbot.view.ui.components.BotMessage
import com.example.archiveandroid.feature.home.chatbot.view.ui.components.ChatInputBar
import com.example.archiveandroid.feature.home.chatbot.view.ui.components.ChatbotHeader
import com.example.archiveandroid.feature.home.chatbot.view.ui.components.SuggestionButtons
import com.example.archiveandroid.feature.home.chatbot.view.ui.components.UserMessage

@Composable
fun ChatbotScreen(
    viewModel: ChatbotViewModel = hiltViewModel()
) {
    val messages by viewModel.messages.collectAsState()
    val inputText by viewModel.inputText.collectAsState()
    val listState = rememberLazyListState()

    // 메시지가 추가되면 자동으로 스크롤
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            ChatbotHeader()
        },
        bottomBar = {
            ChatInputBar(
                value = inputText,
                onValueChange = viewModel::updateInputText,
                onSend = viewModel::sendMessage
            )
        }
    ) { paddingValues ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(messages) { message ->
                when (message.type) {
                    MessageType.BOT -> BotMessage(message.content)
                    MessageType.USER -> UserMessage(message.content)
                    MessageType.SUGGESTION -> SuggestionButtons(
                        suggestions = message.suggestions,
                        onSuggestionClick = { chip ->
                            // 제안 칩의 텍스트에서 이모지 제거
                            val text = chip.text.replace(Regex("[🏃✈️🏛️]\\s*"), "")
                            viewModel.sendSuggestion(text)
                        }
                    )
                }
            }
        }
    }
}
