package com.example.archiveandroid.core.repository

import com.example.archiveandroid.core.network.ApiResultHandler
import com.example.archiveandroid.core.network.dto.ApiResponse
import retrofit2.Response

abstract class BaseRepository {
    
    protected suspend inline fun <T> handleApiCall(
        apiCall: suspend () -> Response<ApiResponse<T>>
    ): Result<T> {
        return try {
            val response = apiCall()
            ApiResultHandler.handleApiResponse(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    protected suspend inline fun <T> handleApiCallWithTransform(
        apiCall: suspend () -> Response<ApiResponse<T>>,
        transform: (T) -> T
    ): Result<T> {
        return try {
            val response = apiCall()
            ApiResultHandler.handleApiResponse(response) { data ->
                Result.success(transform(data))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    protected suspend fun handleUnitApiCall(
        apiCall: suspend () -> Response<ApiResponse<Unit>>
    ): Result<Unit> {
        return try {
            val response = apiCall()
            ApiResultHandler.handleUnitResponse(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
