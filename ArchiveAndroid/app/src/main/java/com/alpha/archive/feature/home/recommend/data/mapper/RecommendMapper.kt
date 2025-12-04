package com.alpha.archive.feature.home.recommend.data.mapper

import com.alpha.archive.core.ui.components.DetailScreenData
import com.alpha.archive.core.ui.components.ListItem
import com.alpha.archive.core.util.CategoryColorGenerator
import com.alpha.archive.core.util.CategoryMapper
import com.alpha.archive.core.util.DateFormatter
import com.alpha.archive.feature.home.recommend.data.remote.dto.RecommendActivityDetailDto
import com.alpha.archive.feature.home.recommend.data.remote.dto.RecommendActivityDto

/**
 * 추천 관련 DTO를 UI 모델로 변환하는 매퍼
 */
object RecommendMapper {
    
    /**
     * 추천 활동 DTO를 ListItem으로 변환
     */
    fun RecommendActivityDto.toListItem(): ListItem {
        val categoryDisplayName = CategoryMapper.toKorean(this.category)
        val (bgColor, fgColor) = CategoryColorGenerator.getCategoryColors(categoryDisplayName)
        val dateText = DateFormatter.formatDateRange(this.startAt, this.endAt)
        val locationText = formatLocation(placeName, placeDistrict)
        
        return ListItem(
            id = this.id,
            title = this.title,
            location = locationText,
            categoryLabel = categoryDisplayName,
            categoryBg = bgColor,
            categoryFg = fgColor,
            thumbnailImageUrl = null,
            date = dateText,
            recommendationReason = null,
            startAt = this.startAt,
            endAt = this.endAt
        )
    }
    
    /**
     * 추천 활동 상세 DTO를 DetailScreenData로 변환
     */
    fun RecommendActivityDetailDto.toDetailScreenData(): DetailScreenData {
        val categoryDisplayName = CategoryMapper.toKorean(this.category)
        val (bgColor, fgColor) = CategoryColorGenerator.getCategoryColors(categoryDisplayName)
        val dateText = DateFormatter.formatDateRange(this.startAt, this.endAt)
        val locationText = formatLocation(placeName, placeDistrict)
        
        val imageUrls = thumbnailImageUrl?.let { listOf(it) } ?: emptyList()
        
        val memoText = if (this.description.isNullOrEmpty()) {
            ""
        } else {
            buildString {
                append(description)
                append("\n\n")
                placeAddress?.takeIf { it.isNotEmpty() }?.let { 
                    append("📍 주소: $it\n") 
                }
                placePhone?.takeIf { it.isNotEmpty() }?.let { 
                    append("📞 전화: $it\n") 
                }
                placeHomepage?.takeIf { it.isNotEmpty() }?.let { 
                    append("🔗 홈페이지: $it\n") 
                }
            }
        }
        
        return DetailScreenData(
            title = this.title,
            categoryDisplayName = categoryDisplayName,
            activityDate = dateText,
            location = locationText,
            memo = memoText,
            images = imageUrls,
            recommendationReason = null,
            categoryBg = bgColor,
            categoryFg = fgColor
        )
    }
    
    /**
     * 장소 정보를 텍스트로 포맷팅
     */
    private fun formatLocation(placeName: String?, placeDistrict: String?): String {
        return when {
            placeName != null && placeDistrict != null -> "$placeName ($placeDistrict)"
            placeName != null -> placeName
            placeDistrict != null -> placeDistrict
            else -> "위치 미정"
        }
    }
}

