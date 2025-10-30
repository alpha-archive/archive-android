package com.alpha.archive.core.network

import com.alpha.archive.core.network.dto.ApiResponse
import retrofit2.Response
import java.io.IOException
import java.net.HttpURLConnection

object ApiResultHandler {
    
    /**
     * HTTP 응답을 AppError로 매핑하여 Result<T> 반환
     */
    fun <T> handleApiResponse(
        response: Response<ApiResponse<T>>,
        onSuccess: (T) -> Result<T>
    ): Result<T> {
        return try {
            if (response.isSuccessful && response.body()?.success == true) {
                val data = response.body()?.data
                if (data != null) {
                    onSuccess(data)
                } else {
                    Result.failure(AppError.Parsing("No data received") as Throwable)
                }
            } else {
                val appError = mapHttpErrorToAppError(response)
                Result.failure(appError as Throwable)
            }
        } catch (e: Exception) {
            Result.failure(mapExceptionToAppError(e) as Throwable)
        }
    }
    
    fun <T> handleApiResponse(
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
                val appError = mapHttpErrorToAppError(response)
                Result.failure(appError as Throwable)
            }
        } catch (e: Exception) {
            Result.failure(mapExceptionToAppError(e) as Throwable)
        }
    }
    
    /**
     * HTTP 상태 코드를 AppError로 매핑
     */
    internal fun mapHttpErrorToAppError(response: Response<*>): AppError {
        val errorMessage = response.body()?.toString() ?: response.message()
        
        return when (response.code()) {
            HttpURLConnection.HTTP_UNAUTHORIZED -> 
                AppError.Unauthorized(errorMessage)
            HttpURLConnection.HTTP_FORBIDDEN -> 
                AppError.Unauthorized("접근 권한이 없습니다")
            HttpURLConnection.HTTP_INTERNAL_ERROR -> 
                AppError.Server("서버 내부 오류가 발생했습니다")
            HttpURLConnection.HTTP_BAD_GATEWAY, 
            HttpURLConnection.HTTP_UNAVAILABLE -> 
                AppError.Server("서버를 사용할 수 없습니다")
            in 400..499 -> 
                AppError.Network("클라이언트 오류: ${response.code()}")
            in 500..599 -> 
                AppError.Server("서버 오류: ${response.code()}")
            else -> 
                AppError.Unknown("HTTP ${response.code()}: $errorMessage")
        }
    }
    
    /**
     * Exception을 AppError로 매핑
     */
    internal fun mapExceptionToAppError(exception: Exception): AppError {
        return when (exception) {
            is IOException -> 
                AppError.Network("네트워크 연결을 확인해주세요", exception)
            is IllegalStateException -> 
                AppError.Parsing("데이터 형식이 올바르지 않습니다", exception)
            else -> 
                AppError.Unknown(exception.message ?: "알 수 없는 오류", exception)
        }
    }
}
