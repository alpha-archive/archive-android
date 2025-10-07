package com.example.archiveandroid.feature.home.record

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.archiveandroid.feature.home.record.data.repository.ActivityRepository
import com.example.archiveandroid.feature.home.record.data.remote.dto.ActivityDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecordViewModel @Inject constructor(
    private val activityRepository: ActivityRepository
) : ViewModel() {
    
    private val _activities = MutableStateFlow<List<ActivityDto>>(emptyList())
    val activities: StateFlow<List<ActivityDto>> = _activities.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    init {
        loadActivities()
    }
    
    private fun loadActivities(loadingState: MutableStateFlow<Boolean>) {
        viewModelScope.launch {
            loadingState.value = true
            _error.value = null
            
            activityRepository.getActivities()
                .onSuccess { activities ->
                    _activities.value = activities
                }
                .onFailure { exception ->
                    _error.value = exception.message ?: "데이터를 불러올 수 없습니다"
                }
            
            loadingState.value = false
        }
    }
    
    fun loadActivities() {
        loadActivities(_isLoading)
    }
    
    fun refreshActivities() {
        loadActivities(_isRefreshing)
    }
}
