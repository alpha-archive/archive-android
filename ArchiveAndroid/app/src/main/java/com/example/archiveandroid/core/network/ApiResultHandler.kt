package com.example.archiveandroid.core.network

import com.example.archiveandroid.core.network.dto.ApiResponse
import retrofit2.Response

object ApiResultHandler {
    
    inline fun <T> handleApiResponse(
        response: Response<ApiResponse<T>>,
        onSuccess: (T) -> Result<T>
    ): Result<T> {
        return try {
            if (response.isSuccessful && response.body()?.success == true) {
                val data = response.body()?.data
                if (data != null) {
                    onSuccess(data)
                } else {
                    Result.failure(Exception("No data received"))
                }
            } else {
                val errorMessage = response.body()?.message ?: response.message()
                Result.failure(Exception("API Error: $errorMessage"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    inline fun <T> handleApiResponse(
        response: Response<ApiResponse<T>>
    ): Result<T> {
        return handleApiResponse(response) { data ->
            Result.success(data)
        }
    }
    
    fun handleUnitResponse(
        response: Response<ApiResponse<Unit>>
    ): Result<Unit> {
        return try {
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(Unit)
            } else {
                val errorMessage = response.body()?.message ?: response.message()
                Result.failure(Exception("API Error: $errorMessage"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
