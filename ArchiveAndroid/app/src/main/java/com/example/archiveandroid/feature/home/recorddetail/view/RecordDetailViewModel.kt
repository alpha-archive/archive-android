package com.example.archiveandroid.feature.home.recorddetail.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.archiveandroid.feature.home.recorddetail.data.repository.RecordDetailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RecordDetailUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val recordData: Any? = null // TODO: 실제 Record 데이터 타입으로 변경
)

@HiltViewModel
class RecordDetailViewModel @Inject constructor(
    private val repository: RecordDetailRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecordDetailUiState())
    val uiState: StateFlow<RecordDetailUiState> = _uiState.asStateFlow()

    fun loadRecordDetail(recordId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            // TODO: 실제 API 호출로 변경
            // repository.getRecordDetail(recordId)
            //     .onSuccess { record ->
            //         _uiState.value = _uiState.value.copy(
            //             isLoading = false,
            //             recordData = record
            //         )
            //     }
            //     .onFailure { exception ->
            //         _uiState.value = _uiState.value.copy(
            //             isLoading = false,
            //             error = exception.message ?: "로드 실패"
            //         )
            //     }
            
            // 임시 더미 데이터
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                recordData = "Dummy record data for ID: $recordId"
            )
        }
    }
}