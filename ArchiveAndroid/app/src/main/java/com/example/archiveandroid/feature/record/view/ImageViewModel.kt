package com.example.archiveandroid.feature.record.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.archiveandroid.feature.record.data.remote.dto.*
import com.example.archiveandroid.feature.record.data.repository.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject


@HiltViewModel
class ImageViewModel @Inject constructor(
    private val imageRepository: ImageRepository
) : ViewModel() {

    private val _uploadState = MutableStateFlow<ImageUploadResponse?>(null)
    val uploadState: StateFlow<ImageUploadResponse?> = _uploadState

    private val _tempImages = MutableStateFlow<List<ImageData>>(emptyList())
    val tempImages: StateFlow<List<ImageData>> = _tempImages

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    /** 이미지 업로드 */
    fun uploadImage(file: File) {
        viewModelScope.launch {
            try {
                val result = imageRepository.uploadImage(file)
                _uploadState.value = result
                fetchTempImages()
            } catch (exception: Exception) {
                _error.value = exception.localizedMessage
            }
        }
    }

    /** 임시 이미지 목록 조회 */
    fun fetchTempImages() {
        viewModelScope.launch {
            try {
                val result = imageRepository.getTempImages()
                if (result.success) {
                    _tempImages.value = result.data
                } else {
                    _error.value = result.message
                }
            } catch (exception: Exception) {
                _error.value = exception.localizedMessage
            }
        }
    }

    /** 이미지 삭제 */
    fun deleteImage(id: String) {
        viewModelScope.launch {
            try {
                val result = imageRepository.deleteImage(id)
                if (result.success) {
                    fetchTempImages()
                } else {
                    _error.value = result.message
                }
            } catch (exception: Exception) {
                _error.value = exception.localizedMessage
            }
        }
    }
}
