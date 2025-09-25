package com.example.archiveandroid.feature.record.data.repository

import com.example.archiveandroid.feature.record.data.remote.RecordApi
import com.example.archiveandroid.feature.record.data.remote.dto.ActivityCreateRequest
import retrofit2.HttpException
import javax.inject.Inject

class RecordRepositoryImpl @Inject constructor(
    private val api: RecordApi
) : RecordRepository {

    override suspend fun createActivity(req: ActivityCreateRequest): Result<Unit> = runCatching {
        val res = api.createActivity(req)

        if (res.isSuccessful) {
            val body = res.body() ?: throw IllegalStateException("Empty body")
            if (body.success) {
                Unit
            } else {
                throw IllegalStateException(body.message.ifBlank { "Request failed" })
            }
        } else {
            throw HttpException(res)
        }
    }
}
