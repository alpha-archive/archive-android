package com.alpha.archive.core.repository

import com.alpha.archive.core.network.ApiResultHandler
import com.alpha.archive.core.network.dto.ApiResponse
import retrofit2.Response

abstract class BaseRepository {
    
    protected suspend fun <T> handleApiCall(
        apiCall: suspend () -> Response<ApiResponse<T>>
    ): Result<T> {
        return try {
            val response = apiCall()
            ApiResultHandler.handleApiResponse(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    protected suspend fun <T> handleApiCallWithTransform(
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
