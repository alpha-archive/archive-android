package com.example.archiveandroid.feature.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun CenterLabel(text: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, style = MaterialTheme.typography.titleLarge)
    }
}

@Composable fun RecommendScreen() { CenterLabel("추천") }
@Composable fun RecordScreen() { CenterLabel("기록") }
@Composable fun ChatbotScreen() { CenterLabel("챗봇") }
@Composable fun StatsScreen() { CenterLabel("통계") }
@Composable fun UserScreen() { CenterLabel("사용자") }


