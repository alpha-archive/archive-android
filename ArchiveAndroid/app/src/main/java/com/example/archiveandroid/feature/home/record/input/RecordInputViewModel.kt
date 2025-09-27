package com.example.archiveandroid.feature.home.record.input

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.archiveandroid.feature.home.recorddetail.data.remote.dto.ActivityCreateRequest
import com.example.archiveandroid.feature.home.recorddetail.data.remote.dto.ImageData
import com.example.archiveandroid.feature.home.recorddetail.data.repository.RecordDetailRepository
import com.example.archiveandroid.feature.home.recorddetail.data.repository.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

data class RecordInputUiState(
    val submitting: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false,
    val uploadedImages: List<ImageData> = emptyList(),
    val isUploadingImage: Boolean = false
)

@HiltViewModel
class RecordInputViewModel @Inject constructor(
    private val repository: RecordDetailRepository,
    private val imageRepository: ImageRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecordInputUiState())
    val uiState: StateFlow<RecordInputUiState> = _uiState.asStateFlow()

    fun uploadImage(file: File) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isUploadingImage = true, errorMessage = null)
            
            imageRepository.uploadImage(file)
                .onSuccess { imageData ->
                    val currentImages = _uiState.value.uploadedImages
                    _uiState.value = _uiState.value.copy(
                        uploadedImages = currentImages + imageData,
                        isUploadingImage = false
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isUploadingImage = false,
                        errorMessage = "이미지 업로드 실패: ${exception.localizedMessage}"
                    )
                }
        }
    }

    fun removeImage(imageId: String) {
        val currentImages = _uiState.value.uploadedImages
        _uiState.value = _uiState.value.copy(
            uploadedImages = currentImages.filter { it.id != imageId }
        )
    }

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
            // 업로드된 이미지 ID들을 포함한 요청 생성
            val requestWithImages = req.copy(
                imageIds = currentState.uploadedImages.map { it.id }
            )
            
            repository.createActivity(requestWithImages)
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
