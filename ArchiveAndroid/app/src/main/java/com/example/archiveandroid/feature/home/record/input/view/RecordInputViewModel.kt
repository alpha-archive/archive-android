package com.example.archiveandroid.feature.home.record.input

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.archiveandroid.core.util.DateFormatter
import com.example.archiveandroid.feature.home.record.input.data.remote.dto.ImageUploadData
import com.example.archiveandroid.feature.home.record.input.data.remote.dto.RecordInputRequest
import com.example.archiveandroid.feature.home.record.input.data.repository.RecordInputRepository
import com.example.archiveandroid.feature.home.record.input.RecordDraft
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
    val uploadedImages: List<ImageUploadData> = emptyList(),
    val isUploadingImage: Boolean = false,
    val isEditMode: Boolean = false,
    val activityId: String? = null,
    val isLoadingActivity: Boolean = false,
    val draft: RecordDraft? = null,
    val originalImageIds: List<String> = emptyList() // 수정 모드에서 기존 이미지 ID들
)

@HiltViewModel
class RecordInputViewModel @Inject constructor(
    private val repository: RecordInputRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecordInputUiState())
    val uiState: StateFlow<RecordInputUiState> = _uiState.asStateFlow()

    fun loadActivityForEdit(activityId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoadingActivity = true,
                isEditMode = true,
                activityId = activityId,
                errorMessage = null
            )

            repository.getActivityForEdit(activityId)
                .onSuccess { activityDetail ->
                    // ActivityDetailDto를 RecordDraft로 변환
                    val draft = RecordDraft(
                        category = activityDetail.category,
                        title = activityDetail.title,
                        memo = activityDetail.memo ?: "",
                        location = activityDetail.location,
                        activityDate = DateFormatter.extractDateOnly(activityDetail.activityDate), // T 이후 시간 부분 제거
                        rating = activityDetail.rating,
                        imageIds = activityDetail.images.map { it.id }
                    )
                    
                    // 기존 데이터로 UI 상태 업데이트
                    _uiState.value = _uiState.value.copy(
                        isLoadingActivity = false,
                        draft = draft,
                        originalImageIds = activityDetail.images.map { it.id },
                        uploadedImages = activityDetail.images.map { imageInfo ->
                            ImageUploadData(
                                id = imageInfo.id,
                                imageKey = imageInfo.id, // 임시로 id 사용
                                imageUrl = imageInfo.imageUrl,
                                originalFilename = "existing_image.jpg", // 임시값
                                fileSize = 0L, // 임시값
                                contentType = "image/jpeg", // 임시값
                                status = "uploaded" // 임시값
                            )
                        }
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoadingActivity = false,
                        errorMessage = "기존 데이터를 불러올 수 없습니다: ${exception.message}"
                    )
                }
        }
    }

    fun uploadImage(file: File) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isUploadingImage = true, errorMessage = null)

            repository.uploadImage(file)
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

    fun onSaveClicked(req: RecordInputRequest) {
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
            if (currentState.isEditMode && currentState.activityId != null) {
                // 수정 모드: PUT API 호출
                val currentImageIds = currentState.uploadedImages.map { it.id }
                val originalImageIds = currentState.originalImageIds
                
                // 새로 추가된 이미지들
                val addImageIds = currentImageIds.filter { it !in originalImageIds }
                // 제거된 이미지들
                val removeImageIds = originalImageIds.filter { it !in currentImageIds }
                
                val requestWithImages = req.copy(
                    imageIds = currentImageIds
                )
                
                repository.updateRecord(currentState.activityId, requestWithImages, addImageIds, removeImageIds)
                    .onSuccess { 
                        _uiState.value = currentState.copy(
                            submitting = false,
                            isSuccess = true
                        )
                    }
                    .onFailure { exception ->
                        _uiState.value = currentState.copy(
                            submitting = false,
                            errorMessage = exception.message ?: "수정 실패"
                        )
                    }
            } else {
                // 생성 모드: POST API 호출
                val requestWithImages = req.copy(
                    imageIds = currentState.uploadedImages.map { it.id }
                )
                
                repository.createRecord(requestWithImages)
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
}
