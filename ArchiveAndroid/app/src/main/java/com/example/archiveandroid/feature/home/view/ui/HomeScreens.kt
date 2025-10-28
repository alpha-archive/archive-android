package com.example.archiveandroid.feature.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.archiveandroid.feature.home.recommend.view.ui.RecommendScreen

@Composable
fun CenterLabel(text: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, style = MaterialTheme.typography.titleLarge)
    }
}

@Composable 
fun RecommendScreen() { 
    com.example.archiveandroid.feature.home.recommend.view.ui.RecommendScreen()
}
@Composable fun ChatbotScreen() { 
    com.example.archiveandroid.feature.home.chatbot.view.ui.ChatbotScreen()
}
@Composable fun StatsScreen() { com.example.archiveandroid.feature.home.stats.view.ui.StatsScreen() }
@Composable fun UserScreen() { CenterLabel("사용자") }


