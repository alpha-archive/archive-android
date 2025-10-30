package com.alpha.archiveandroid.feature.home.chatbot.view.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.alpha.archiveandroid.feature.home.chatbot.data.model.MessageType
import com.alpha.archiveandroid.feature.home.chatbot.view.ChatbotViewModel
import com.alpha.archiveandroid.feature.home.chatbot.view.ui.components.BotLoadingMessage
import com.alpha.archiveandroid.feature.home.chatbot.view.ui.components.BotMessage
import com.alpha.archiveandroid.feature.home.chatbot.view.ui.components.ChatInputBar
import com.alpha.archiveandroid.feature.home.chatbot.view.ui.components.ChatbotHeader
import com.alpha.archiveandroid.feature.home.chatbot.view.ui.components.SuggestionButtons
import com.alpha.archiveandroid.feature.home.chatbot.view.ui.components.UserMessage

@Composable
fun ChatbotScreen(
    viewModel: ChatbotViewModel = hiltViewModel()
) {
    val messages by viewModel.messages.collectAsState()
    val inputText by viewModel.inputText.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val listState = rememberLazyListState()

    // 메시지 추가/로딩 변경 시 최신으로 스크롤 (reverseLayout 기준 0 인덱스)
    LaunchedEffect(messages.size, isLoading) {
        if (messages.isNotEmpty() || isLoading) {
            listState.animateScrollToItem(0)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        ChatbotHeader()
        Box(modifier = Modifier.weight(1f)) {
            val density = LocalDensity.current
            val imeBottomPx = WindowInsets.ime.getBottom(density)
            val HOME_BOTTOM_BAR_HEIGHT = 80.dp
            val CHAT_INPUT_BAR_HEIGHT = 56.dp
            val CHAT_INPUT_BAR_HEIGHT_PADDING = 28.dp
            val adjustedImeDp = with(density) {
                (imeBottomPx.toDp() - HOME_BOTTOM_BAR_HEIGHT - CHAT_INPUT_BAR_HEIGHT)
                    .coerceAtLeast(0.dp)
            }

            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 12.dp,
                        bottom = CHAT_INPUT_BAR_HEIGHT + CHAT_INPUT_BAR_HEIGHT_PADDING + adjustedImeDp
                    ),
                reverseLayout = true,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (isLoading) {
                    item { BotLoadingMessage() }
                }
                items(messages.reversed()) { message ->
                    when (message.type) {
                        MessageType.BOT -> BotMessage(message.content)
                        MessageType.USER -> UserMessage(message.content)
                        MessageType.SUGGESTION -> SuggestionButtons(
                            suggestions = message.suggestions,
                            onSuggestionClick = { chip ->
                                val text = chip.text.replace(Regex("[🏃✈️🏛️]\\s*"), "")
                                viewModel.sendSuggestion(text)
                            }
                        )
                    }
                }
            }
            ChatInputBar(
                value = inputText,
                onValueChange = viewModel::updateInputText,
                onSend = viewModel::sendMessage,
                isLoading = isLoading,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = adjustedImeDp)
            )
        }
    }
}
