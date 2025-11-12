package com.alpha.archive.feature.home.record.input.domain

import com.alpha.archive.feature.home.record.input.data.remote.dto.RecordInputRequest
import com.alpha.archive.feature.home.record.input.data.repository.RecordInputRepository
import javax.inject.Inject

/**
 * RecordInputViewModel에서 분리한 클래스
 * 활동 기록 생성 및 수정 로직을 담당하는 UseCase
 */
class RecordInputUseCase @Inject constructor(
    private val repository: RecordInputRepository
) {

    suspend fun createRecord(request: RecordInputRequest): Result<Unit> {
        return repository.createRecord(request)
    }

    suspend fun updateRecord(
        activityId: String,
        request: RecordInputRequest,
        addImageIds: List<String>,
        removeImageIds: List<String>
    ): Result<Unit> {
        return repository.updateRecord(activityId, request, addImageIds, removeImageIds)
    }
}