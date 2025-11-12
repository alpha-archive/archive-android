package com.alpha.archive.feature.home.record.input.domain

import com.alpha.archive.feature.home.record.input.data.remote.dto.ImageUploadData
import com.alpha.archive.feature.home.record.input.data.repository.RecordInputRepository
import java.io.File
import javax.inject.Inject

/**
 * RecordInputViewModel에서 분리한 클래스
 * 이미지 업로드를 담당하는 매니저
 */
class ImageUploadManager @Inject constructor(
    private val repository: RecordInputRepository
) {

    suspend fun uploadImage(file: File): Result<ImageUploadData> {
        return repository.uploadImage(file)
    }

}
