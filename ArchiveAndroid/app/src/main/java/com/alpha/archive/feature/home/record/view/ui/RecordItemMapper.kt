package com.alpha.archive.feature.home.record.ui

import com.alpha.archive.core.util.CategoryColorGenerator
import com.alpha.archive.feature.home.record.data.remote.dto.ActivityDto

object RecordItemMapper {
    
    fun ActivityDto.toRecordItem(): RecordItem {
        val (bgColor, fgColor) = CategoryColorGenerator.getCategoryColors(categoryDisplayName)
        
        return RecordItem(
            id = id,
            title = title,
            location = location ?: "",
            categoryLabel = categoryDisplayName,
            categoryBg = bgColor,
            categoryFg = fgColor,
            imagePainter = null,
            thumbnailImageUrl = thumbnailImageUrl
        )
    }
}
