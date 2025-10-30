package com.alpha.archive.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.alpha.archive.core.util.DateFormatter
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

data class ListItem(
    val id: String,
    val title: String,
    val location: String,
    val categoryLabel: String,
    val categoryBg: Color,
    val categoryFg: Color,
    val imagePainter: Painter? = null,
    val thumbnailImageUrl: String? = null,
    val date: String? = null,
    val recommendationReason: String? = null, // 추천 이유 (추천 기능용)
    val startAt: String? = null, // 시작일 (yyyy-MM-dd 형식)
    val endAt: String? = null // 종료일 (yyyy-MM-dd 형식)
)

@Composable
fun ListItemCard(
    item: ListItem,
    onClick: (ListItem) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val dDayText = calculateDDay(item.startAt, item.endAt)
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick(item) },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // 제목과 카테고리
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .basicMarquee(),
                    maxLines = 1,
                    color = Color.Black
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                // 카테고리 태그
                Box(
                    modifier = Modifier
                        .background(
                            item.categoryBg,
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = item.categoryLabel,
                        color = item.categoryFg,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Medium,
                        )
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 일자와 D-day
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "일자",
                    style = MaterialTheme.typography.bodyMedium.copy(
                    ),
                    color = Color(0xFF666666)
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                if (dDayText != null) {
                    Text(
                        text = dDayText,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                        ),
                        color = Color(0xFFFF6B6B)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            // 시작일 ~ 종료일
            if (item.startAt != null && item.endAt != null) {
                Text(
                    text = "${DateFormatter.formatToDisplayDate(item.startAt)} ~ ${DateFormatter.formatToDisplayDate(item.endAt)}",
                    style = MaterialTheme.typography.bodySmall.copy(
                    ),
                    color = Color(0xFF888888)
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 위치
            Text(
                text = item.location,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.basicMarquee(),
                color = Color(0xFF666666),
                maxLines = 1
            )
        }
    }
}

/**
 * D-day 계산 함수
 */
private fun calculateDDay(startAt: String?, endAt: String?): String? {
    if (startAt == null || endAt == null) return null
    
    try {
        // T 이후 시간 부분 제거
        val startDateOnly = DateFormatter.extractDateOnly(startAt)
        val endDateOnly = DateFormatter.extractDateOnly(endAt)
        
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val today = LocalDate.now()
        val startDate = LocalDate.parse(startDateOnly, formatter)
        val endDate = LocalDate.parse(endDateOnly, formatter)
        
        return when {
            today.isBefore(startDate) -> {
                // 시작일 전
                val daysUntil = ChronoUnit.DAYS.between(today, startDate)
                "D-$daysUntil"
            }
            today.isAfter(endDate) -> {
                // 종료일 이후
                val daysSince = ChronoUnit.DAYS.between(endDate, today)
                "D+$daysSince"
            }
            else -> {
                // 시작일과 종료일 사이 (진행 중)
                "D-day"
            }
        }
    } catch (e: Exception) {
        return null
    }
}

