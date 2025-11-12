package com.alpha.archive.feature.home.record.input.domain

import com.alpha.archive.feature.home.record.input.data.remote.dto.RecordInputRequest

/**
 * RecordInputViewModel에서 분리한 클래스
 * RecordInput 화면의 폼 입력값 검증을 담당하는 Validator
 */
object RecordFormValidator {

    fun validate(request: RecordInputRequest): String? {
        return when {
            request.title.isBlank() -> "활동명을 입력해주세요!"
            request.category.isBlank() -> "카테고리를 선택해주세요!"
            else -> null
        }
    }
}
