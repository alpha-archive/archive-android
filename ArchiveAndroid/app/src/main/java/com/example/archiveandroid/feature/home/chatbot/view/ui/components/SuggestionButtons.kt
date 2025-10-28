package com.example.archiveandroid.feature.home.chatbot.view.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.archiveandroid.feature.home.chatbot.data.model.SuggestionChip

@Composable
fun SuggestionButtons(
    suggestions: List<SuggestionChip>,
    onSuggestionClick: (SuggestionChip) -> Unit = {}
) {
    if (suggestions.isEmpty()) return
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        // 봇 아이콘 공간 맞추기
        Spacer(modifier = Modifier.width(48.dp))
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(suggestions) { suggestion ->
                OutlinedButton(
                    onClick = { onSuggestionClick(suggestion) },
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = suggestion.color.copy(alpha = 0.1f),
                        contentColor = suggestion.color
                    ),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        width = 1.dp,
                        brush = androidx.compose.ui.graphics.SolidColor(suggestion.color)
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.height(40.dp)
                ) {
                    Text(
                        text = suggestion.text,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

