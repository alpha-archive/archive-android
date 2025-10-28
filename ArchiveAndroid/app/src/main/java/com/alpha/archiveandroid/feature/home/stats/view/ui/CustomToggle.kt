package com.alpha.archiveandroid.feature.home.stats.view.ui

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun CustomToggle(
    isWeekly: Boolean,
    onToggleChange: ((Boolean) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val offsetFraction by animateFloatAsState(
        targetValue = if (isWeekly) 0f else 0.5f,
        animationSpec = tween(durationMillis = 300),
        label = "toggle_animation"
    )

    BoxWithConstraints(
        modifier = modifier
            .width(96.dp)
            .height(48.dp)
            .background(
                Color(0xFFE0E0E0),
                RoundedCornerShape(8.dp)
            )
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onToggleChange?.invoke(!isWeekly)
            }
            .padding(4.dp)
    ) {
        val maxWidthPx = constraints.maxWidth

        // 흰색 이동 박스
        Box(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .height(40.dp)
                .offset {
                    IntOffset(
                        x = (maxWidthPx * offsetFraction).toInt(),
                        y = 0
                    )
                }
                .background(
                    Color.White,
                    RoundedCornerShape(6.dp)
                )
        )

        // 텍스트들
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "주간",
                    color = if (isWeekly) Color(0xFF333333) else Color(0xFF666666),
                    fontWeight = if (isWeekly) FontWeight.Bold else FontWeight.Normal,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "월간",
                    color = if (!isWeekly) Color(0xFF333333) else Color(0xFF666666),
                    fontWeight = if (!isWeekly) FontWeight.Bold else FontWeight.Normal,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
