package com.example.archiveandroid.feature.home.recommend.detail.view

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.archiveandroid.core.ui.components.DetailScreenState
import com.example.archiveandroid.core.util.DateFormatter
import com.example.archiveandroid.feature.home.recommend.data.repository.RecommendRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecommendDetailViewModel @Inject constructor(
    private val recommendRepository: RecommendRepository
) : ViewModel() {
    
    private var activityId: String = ""
    
    private val _uiState = MutableStateFlow(
        DetailScreenState(isLoading = true)
    )
    val uiState: StateFlow<DetailScreenState> = _uiState.asStateFlow()
    
    fun loadActivityDetail(activityId: String) {
        this.activityId = activityId
        
        if (activityId.isEmpty()) {
            _uiState.value = DetailScreenState(
                isLoading = false,
                error = "활동 ID가 없습니다"
            )
            return
        }
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            recommendRepository.getRecommendActivityDetail(activityId)
                .onSuccess { detailDto ->
                    val detailData = detailDto.toDetailScreenData()
                    _uiState.value = DetailScreenState(
                        isLoading = false,
                        data = detailData
                    )
                }
                .onFailure { exception ->
                    _uiState.value = DetailScreenState(
                        isLoading = false,
                        error = exception.message ?: "활동 정보를 불러올 수 없습니다"
                    )
                }
        }
    }
    
    fun refresh() {
        if (activityId.isNotEmpty()) {
            loadActivityDetail(activityId)
        }
    }
}

/**
 * RecommendActivityDetailDto를 DetailScreenData로 변환
 */
private fun com.example.archiveandroid.feature.home.recommend.data.remote.dto.RecommendActivityDetailDto.toDetailScreenData(): com.example.archiveandroid.core.ui.components.DetailScreenData {
    // 날짜 포맷팅
    val dateText = DateFormatter.formatDateRange(this.startAt, this.endAt)
    
    // 이미지 URL 리스트 생성 (썸네일이 있으면 사용)
    val imageUrls = if (this.thumbnailImageUrl != null) {
        listOf(this.thumbnailImageUrl)
    } else {
        emptyList()
    }
    
    // 카테고리 표시명 (영문 카테고리를 한글로 변환)
    val categoryDisplayName = getCategoryDisplayName(this.category)
    
    // 카테고리 색상
    val (bgColor, fgColor) = getCategoryColor(categoryDisplayName)
    
    // 장소 정보 조합
    val locationText = "${this.placeName} (${this.placeDistrict})"
    
    // 상세 정보 조합 (description이 없으면 상세정보 전체를 띄우지 않음)
    val memoText = if (this.description.isNullOrEmpty()) {
        ""
    } else {
        buildString {
            append(this@toDetailScreenData.description)
            append("\n\n")
            append("📍 주소: ${this@toDetailScreenData.placeAddress}\n")
            if (!this@toDetailScreenData.placePhone.isNullOrEmpty()) {
                append("📞 전화: ${this@toDetailScreenData.placePhone}\n")
            }
            if (!this@toDetailScreenData.placeHomepage.isNullOrEmpty()) {
                append("🔗 홈페이지: ${this@toDetailScreenData.placeHomepage}\n")
            }
        }
    }
    
    return com.example.archiveandroid.core.ui.components.DetailScreenData(
        title = this.title,
        categoryDisplayName = categoryDisplayName,
        activityDate = dateText,
        location = locationText,
        memo = memoText,
        images = imageUrls,
        recommendationReason = null, // API 명세서에 없음
        categoryBg = bgColor,
        categoryFg = fgColor
    )
}

/**
 * 영문 카테고리를 한글 표시명으로 변환
 */
private fun getCategoryDisplayName(category: String): String {
    return when (category.uppercase()) {
        "MUSICAL" -> "뮤지컬"
        "THEATER" -> "연극"
        "MOVIE" -> "영화"
        "EXHIBITION" -> "전시"
        "COOKING" -> "요리"
        "VOLUNTEER" -> "봉사"
        "READING" -> "독서"
        "CONCERT" -> "콘서트"
        "FESTIVAL" -> "축제"
        "WORKSHOP" -> "워크샵"
        "SPORTS" -> "스포츠"
        "TRAVEL" -> "여행"
        "OUTDOOR" -> "야외활동"
        "HOBBY" -> "취미"
        "STUDY" -> "스터디"
        "NETWORKING" -> "네트워킹"
        "EXPERIENCE" -> "체험"
        "OTHER" -> "기타"
        else -> category
    }
}

/**
 * 카테고리에 따른 색상 반환
 */
private fun getCategoryColor(category: String): Pair<Color, Color> {
    return when (category) {
        "여행" -> Pair(Color(0xFFE8F0FF), Color(0xFF335C99))
        "독서" -> Pair(Color(0xFFFFEFE6), Color(0xFFB04A17))
        "스포츠", "스포츠 관람" -> Pair(Color(0xFFEAF5FF), Color(0xFF2C5A8A))
        "운동" -> Pair(Color(0xFFE7FFF2), Color(0xFF138A52))
        "전시" -> Pair(Color(0xFFFFF4E5), Color(0xFF9A6B1A))
        "봉사" -> Pair(Color(0xFFEFF7FF), Color(0xFF2D6AA3))
        "뮤지컬" -> Pair(Color(0xFFF4E9FF), Color(0xFF6B39A6))
        "악기 연주" -> Pair(Color(0xFFEFF9FF), Color(0xFF1F6E8C))
        "연극" -> Pair(Color(0xFFFFF0F5), Color(0xFF8B4067))
        "영화" -> Pair(Color(0xFFE8E8E8), Color(0xFF4A4A4A))
        "콘서트" -> Pair(Color(0xFFFFE8F0), Color(0xFF993366))
        "축제" -> Pair(Color(0xFFFFFBE6), Color(0xFF996600))
        "체험" -> Pair(Color(0xFFE6F9FF), Color(0xFF006699))
        "요리" -> Pair(Color(0xFFFFF3E0), Color(0xFFE65100))
        "워크샵" -> Pair(Color(0xFFF3E5F5), Color(0xFF7B1FA2))
        "야외활동" -> Pair(Color(0xFFE8F5E9), Color(0xFF2E7D32))
        "취미" -> Pair(Color(0xFFFCE4EC), Color(0xFFC2185B))
        "스터디" -> Pair(Color(0xFFE3F2FD), Color(0xFF1565C0))
        "네트워킹" -> Pair(Color(0xFFFFF9C4), Color(0xFFF57F17))
        "기타" -> Pair(Color(0xFFECEFF1), Color(0xFF546E7A))
        else -> Pair(Color(0xFFE0E0E0), Color(0xFF666666))
    }
}
