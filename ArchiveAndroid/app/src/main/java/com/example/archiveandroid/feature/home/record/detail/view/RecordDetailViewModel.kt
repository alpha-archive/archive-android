package com.example.archiveandroid.feature.home.recorddetail.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.archiveandroid.feature.home.record.data.repository.ActivityRepository
import com.example.archiveandroid.feature.home.record.data.remote.dto.ActivityDetailDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RecordDetailUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val recordData: ActivityDetailDto? = null
)

@HiltViewModel
class RecordDetailViewModel @Inject constructor(
    private val repository: ActivityRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecordDetailUiState())
    val uiState: StateFlow<RecordDetailUiState> = _uiState.asStateFlow()

    fun loadRecordDetail(recordId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            try {
                repository.getActivity(recordId)
                    .onSuccess { record ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            recordData = record
                        )
                    }
                    .onFailure { exception ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = exception.message ?: "로드 실패"
                        )
                    }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "예외 발생: ${e.message}"
                )
            }
        }
    }
}