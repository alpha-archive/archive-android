package com.alpha.archive.feature.home.record.input

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alpha.archive.core.network.toUserFriendlyMessage
import com.alpha.archive.core.util.DateFormatter
import com.alpha.archive.feature.home.record.input.data.remote.dto.ImageUploadData
import com.alpha.archive.feature.home.record.input.data.remote.dto.RecordInputRequest
import com.alpha.archive.feature.home.record.input.data.repository.RecordInputRepository
import com.alpha.archive.feature.home.record.input.domain.ImageUploadManager
import com.alpha.archive.feature.home.record.input.domain.RecordFormValidator
import com.alpha.archive.feature.home.record.input.domain.RecordInputUseCase
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
    private val repository: RecordInputRepository,
    private val inputUseCase: RecordInputUseCase,
    private val imageUploadManager: ImageUploadManager
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
                        location = activityDetail.location ?: "",
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
                        errorMessage = exception.toUserFriendlyMessage()
                    )
                }
        }
    }

    fun uploadImage(file: File) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isUploadingImage = true, errorMessage = null)

            imageUploadManager.uploadImage(file)
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
                        errorMessage = "이미지 업로드 중 오류가 발생했습니다"
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

        val validationError = RecordFormValidator.validate(req)
        if (validationError != null) {
            _uiState.value = currentState.copy(
                submitting = false,
                errorMessage = validationError
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
                
                inputUseCase.updateRecord(currentState.activityId, requestWithImages, addImageIds, removeImageIds)
                    .onSuccess { 
                        _uiState.value = currentState.copy(
                            submitting = false,
                            isSuccess = true
                        )
                    }
                    .onFailure { exception ->
                        _uiState.value = currentState.copy(
                            submitting = false,
                            errorMessage = exception.toUserFriendlyMessage()
                        )
                    }
            } else {
                // 생성 모드: POST API 호출
                val requestWithImages = req.copy(
                    imageIds = currentState.uploadedImages.map { it.id }
                )
                
                inputUseCase.createRecord(requestWithImages)
                    .onSuccess { 
                        _uiState.value = currentState.copy(
                            submitting = false,
                            isSuccess = true
                        )
                    }
                    .onFailure { exception ->
                        _uiState.value = currentState.copy(
                            submitting = false,
                            errorMessage = exception.toUserFriendlyMessage()
                        )
                    }
            }
        }
    }
}
