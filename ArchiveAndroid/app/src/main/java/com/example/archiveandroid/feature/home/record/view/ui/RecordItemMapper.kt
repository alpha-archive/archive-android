package com.example.archiveandroid.feature.home.record.ui

import androidx.compose.ui.graphics.Color
import com.example.archiveandroid.feature.home.record.data.remote.dto.ActivityDto

object RecordItemMapper {
    
    private val categoryColors = mapOf(
        "여행" to Pair(Color(0xFFE8F0FF), Color(0xFF335C99)),
        "독서" to Pair(Color(0xFFFFEFE6), Color(0xFFB04A17)),
        "스포츠 관람" to Pair(Color(0xFFEAF5FF), Color(0xFF2C5A8A)),
        "운동" to Pair(Color(0xFFE7FFF2), Color(0xFF138A52)),
        "전시" to Pair(Color(0xFFFFF4E5), Color(0xFF9A6B1A)),
        "봉사" to Pair(Color(0xFFEFF7FF), Color(0xFF2D6AA3)),
        "뮤지컬" to Pair(Color(0xFFF4E9FF), Color(0xFF6B39A6)),
        "악기 연주" to Pair(Color(0xFFEFF9FF), Color(0xFF1F6E8C))
    )
    
    fun ActivityDto.toRecordItem(): RecordItem {
        val (bgColor, fgColor) = categoryColors[categoryDisplayName] ?: Pair(Color(0xFFE0E0E0), Color(0xFF666666))
        
        return RecordItem(
            id = id,
            title = title,
            location = location,
            categoryLabel = categoryDisplayName,
            categoryBg = bgColor,
            categoryFg = fgColor,
            imagePainter = null,
            thumbnailImageUrl = thumbnailImageUrl
        )
    }
}
