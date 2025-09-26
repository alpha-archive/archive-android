package com.example.archiveandroid.feature.home.recorddetail.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.archiveandroid.feature.home.recorddetail.data.remote.dto.ActivityCreateRequest
import com.example.archiveandroid.feature.home.recorddetail.data.repository.RecordDetailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RecordDetailUiState(
    val submitting: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
)

@HiltViewModel
class RecordDetailViewModel @Inject constructor(
    private val repository: RecordDetailRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecordDetailUiState())
    val uiState: StateFlow<RecordDetailUiState> = _uiState.asStateFlow()

    fun onSaveClicked(req: ActivityCreateRequest) {
        val currentState = _uiState.value
        if (currentState.submitting) return

        if (req.title.isBlank()) {
            _uiState.value = currentState.copy(
                submitting = false,
                errorMessage = "활동명을 입력해주세요!"
            )
            return
        }
        if (req.category.isBlank()) {
            _uiState.value = currentState.copy(
                submitting = false,
                errorMessage = "카테고리를 선택해주세요!"
            )
            return
        }

        _uiState.value = currentState.copy(submitting = true, errorMessage = null)

        viewModelScope.launch {
            repository.createActivity(req)
                .onSuccess { 
                    _uiState.value = currentState.copy(
                        submitting = false,
                        isSuccess = true
                    )
                }
                .onFailure { exception ->
                    _uiState.value = currentState.copy(
                        submitting = false,
                        errorMessage = exception.message ?: "저장 실패"
                    )
                }
        }
    }
}
