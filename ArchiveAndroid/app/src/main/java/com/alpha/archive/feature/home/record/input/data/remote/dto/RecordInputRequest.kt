package com.alpha.archive.feature.home.record.input.data.remote.dto

data class RecordInputRequest(
    val title: String,
    val category: String,
    val location: String?,
    val activityDate: String?,
    val rating: Int?,
    val memo: String?,
    val imageIds: List<String> = emptyList(),
    // val publicEventId: String? = null
) {
    fun validate(): String? {
        if (title.isBlank()) return "활동명을 입력해주세요!"
        if (category.isBlank()) return "카테고리를 선택해주세요!"
        if (activityDate.isNullOrBlank()) return "활동 날짜를 선택해주세요!"
        if (rating !in 1..5) return "평점을 입력해주세요!"

        return null
    }
}
