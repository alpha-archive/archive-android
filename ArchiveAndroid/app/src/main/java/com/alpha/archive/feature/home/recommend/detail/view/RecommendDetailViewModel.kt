package com.alpha.archive.feature.home.recommend.detail.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alpha.archive.core.network.toUserFriendlyMessage
import com.alpha.archive.core.ui.components.DetailScreenState
import com.alpha.archive.core.util.CategoryColorGenerator
import com.alpha.archive.core.util.CategoryMapper
import com.alpha.archive.core.util.DateFormatter
import com.alpha.archive.feature.home.recommend.data.repository.RecommendRepository
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
                        error = exception.toUserFriendlyMessage()
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
private fun com.alpha.archive.feature.home.recommend.data.remote.dto.RecommendActivityDetailDto.toDetailScreenData(): com.alpha.archive.core.ui.components.DetailScreenData {
    // 날짜 포맷팅
    val dateText = DateFormatter.formatDateRange(this.startAt, this.endAt)
    
    // 이미지 URL 리스트 생성 (썸네일이 있으면 사용)
    val imageUrls = if (this.thumbnailImageUrl != null) {
        listOf(this.thumbnailImageUrl)
    } else {
        emptyList()
    }
    
    // 카테고리 표시명 (영문 카테고리를 한글로 변환)
    val categoryDisplayName = CategoryMapper.toKorean(this.category)
    
    // 카테고리 색상
    val (bgColor, fgColor) = CategoryColorGenerator.getCategoryColors(categoryDisplayName)
    
    // 장소 정보 조합 (null 처리)
    val locationText = when {
        placeName != null && placeDistrict != null -> "$placeName ($placeDistrict)"
        placeName != null -> placeName
        placeDistrict != null -> placeDistrict
        else -> "위치 미정"
    }
    
    // 상세 정보 조합 (description이 없으면 상세정보 전체를 띄우지 않음)
    val memoText = if (this.description.isNullOrEmpty()) {
        ""
    } else {
        buildString {
            append(this@toDetailScreenData.description)
            append("\n\n")
            if (!this@toDetailScreenData.placeAddress.isNullOrEmpty()) {
                append("📍 주소: ${this@toDetailScreenData.placeAddress}\n")
            }
            if (!this@toDetailScreenData.placePhone.isNullOrEmpty()) {
                append("📞 전화: ${this@toDetailScreenData.placePhone}\n")
            }
            if (!this@toDetailScreenData.placeHomepage.isNullOrEmpty()) {
                append("🔗 홈페이지: ${this@toDetailScreenData.placeHomepage}\n")
            }
        }
    }
    
    return com.alpha.archive.core.ui.components.DetailScreenData(
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

