package com.example.archiveandroid.feature.home.recorddetail.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.archiveandroid.feature.home.recorddetail.data.remote.dto.ImageData
import com.example.archiveandroid.feature.home.recorddetail.data.repository.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

data class ImageUiState(
    val uploadState: ImageData? = null,
    val tempImages: List<ImageData> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class ImageViewModel @Inject constructor(
    private val imageRepository: ImageRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ImageUiState())
    val uiState: StateFlow<ImageUiState> = _uiState.asStateFlow()

    fun uploadImage(file: File) {
        viewModelScope.launch {
            imageRepository.uploadImage(file)
                .onSuccess { imageData ->
                    _uiState.value = _uiState.value.copy(
                        uploadState = imageData,
                        error = null
                    )
                    fetchTempImages()
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        error = exception.localizedMessage
                    )
                }
        }
    }

    fun fetchTempImages() {
        viewModelScope.launch {
            imageRepository.getTempImages()
                .onSuccess { images ->
                    _uiState.value = _uiState.value.copy(
                        tempImages = images,
                        error = null
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        error = exception.localizedMessage
                    )
                }
        }
    }

    fun deleteImage(id: String) {
        viewModelScope.launch {
            imageRepository.deleteImage(id)
                .onSuccess {
                    fetchTempImages()
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        error = exception.localizedMessage
                    )
                }
        }
    }
}
