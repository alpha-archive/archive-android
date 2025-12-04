package com.alpha.archive.feature.home.recommend.detail.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alpha.archive.core.network.toUserFriendlyMessage
import com.alpha.archive.core.ui.components.DetailScreenState
import com.alpha.archive.feature.home.recommend.data.mapper.RecommendMapper.toDetailScreenData
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

