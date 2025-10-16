package com.example.archiveandroid.feature.home.recorddetail.data.repository

interface RecordDetailRepository {
    suspend fun getActivityDetail(id: String): Result<Any> // TODO: 실제 RecordDetail DTO로 교체
}
