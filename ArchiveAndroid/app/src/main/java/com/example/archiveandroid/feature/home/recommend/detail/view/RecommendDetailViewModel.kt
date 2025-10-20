package com.example.archiveandroid.feature.home.recommend.detail.view

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
    private val recommendRepository: RecommendRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    
    private val activityId = savedStateHandle.get<String>("activityId") ?: ""
    
    private val _uiState = MutableStateFlow(
        DetailScreenState(isLoading = true)
    )
    val uiState: StateFlow<DetailScreenState> = _uiState.asStateFlow()
    
    init {
        loadActivityDetail()
    }
    
    private fun loadActivityDetail() {
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
                        error = exception.message ?: "상세 정보를 불러올 수 없습니다"
                    )
                }
        }
    }
    
    fun refresh() {
        loadActivityDetail()
    }
}

/**
 * RecommendActivityDetailDto를 DetailScreenData로 변환
 */
private fun com.example.archiveandroid.feature.home.recommend.data.remote.dto.RecommendActivityDetailDto.toDetailScreenData(): com.example.archiveandroid.core.ui.components.DetailScreenData {
    // 날짜 포맷팅
    val dateText = DateFormatter.formatDateRange(this.startDate, this.endDate)
    
    // 이미지 URL 리스트 생성
    val imageUrls = this.images.map { it.imageUrl }
    
    return com.example.archiveandroid.core.ui.components.DetailScreenData(
        title = this.title,
        categoryDisplayName = this.categoryDisplayName,
        activityDate = dateText,
        location = this.location,
        memo = this.description,
        images = imageUrls,
        recommendationReason = this.recommendationReason
    )
}
