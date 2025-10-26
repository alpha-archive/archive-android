package com.example.archiveandroid.feature.home.stats.view

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.archiveandroid.feature.home.stats.data.ActivityTypeData
import com.example.archiveandroid.feature.home.stats.data.repository.StatsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val statsRepository: StatsRepository
) : ViewModel() {

    private val _totalActivities = MutableStateFlow(0)
    val totalActivities: StateFlow<Int> = _totalActivities.asStateFlow()

    private val _categoryStats = MutableStateFlow<List<ActivityTypeData>>(emptyList())
    val categoryStats: StateFlow<List<ActivityTypeData>> = _categoryStats.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadStatistics()
    }

    fun loadStatistics() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            statsRepository.getOverallStatistics()
                .onSuccess { response ->
                    _totalActivities.value = response.totalActivities
                    _categoryStats.value = response.categoryStats.map { stat ->
                        ActivityTypeData(
                            type = stat.displayName,
                            count = stat.count,
                            color = getCategoryColor(stat.category)
                        )
                    }
                }
                .onFailure { throwable ->
                    _error.value = throwable.message ?: "통계를 불러오는데 실패했습니다."
                }

            _isLoading.value = false
        }
    }

    private fun getCategoryColor(category: String): Color {
        // 카테고리 이름을 시드로 사용해서 일관된 랜덤 색상 생성
        val hash = category.hashCode()
        
        // Hue (색상): 0-360도 범위에서 선택
        val hue = kotlin.math.abs(hash) % 360
        
        // Saturation (채도): 60-90% 범위로 제한 (너무 연하지 않게)
        val saturation = 0.6f + (kotlin.math.abs(hash shr 8) % 31) / 100f
        
        // Value (명도): 70-95% 범위로 제한 (너무 어둡지 않게)
        val value = 0.7f + (kotlin.math.abs(hash shr 16) % 26) / 100f
        
        return Color.hsv(hue.toFloat(), saturation, value)
    }
}

