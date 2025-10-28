package com.alpha.archiveandroid.feature.home.record.ui

import com.alpha.archiveandroid.core.util.CategoryColorGenerator
import com.alpha.archiveandroid.feature.home.record.data.remote.dto.ActivityDto

object RecordItemMapper {
    
    fun ActivityDto.toRecordItem(): RecordItem {
        val (bgColor, fgColor) = CategoryColorGenerator.getCategoryColors(categoryDisplayName)
        
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
