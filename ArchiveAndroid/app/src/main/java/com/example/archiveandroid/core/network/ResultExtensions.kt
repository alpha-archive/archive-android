package com.example.archiveandroid.core.network

import kotlin.Result

/**
 * Result<T> 확장 함수들
 * AppError 처리를 위한 편의 함수들
 */
inline fun <T> Result<T>.foldAppError(
    onSuccess: (T) -> Unit,
    onNetworkError: (AppError.Network) -> Unit = {},
    onUnauthorizedError: (AppError.Unauthorized) -> Unit = {},
    onServerError: (AppError.Server) -> Unit = {},
    onParsingError: (AppError.Parsing) -> Unit = {},
    onUnknownError: (AppError.Unknown) -> Unit = {},
    onOtherError: (Throwable) -> Unit = {}
) {
    fold(
        onSuccess = onSuccess,
        onFailure = { error ->
            when (error) {
                is AppError.Network -> onNetworkError(error)
                is AppError.Unauthorized -> onUnauthorizedError(error)
                is AppError.Server -> onServerError(error)
                is AppError.Parsing -> onParsingError(error)
                is AppError.Unknown -> onUnknownError(error)
                else -> onOtherError(error)
            }
        }
    )
}

/**
 * AppError인지 확인
 */
fun Result<*>.isAppError(): Boolean {
    return isFailure && exceptionOrNull() is AppError
}

/**
 * 특정 AppError 타입인지 확인
 */
inline fun <reified T : AppError> Result<*>.isAppErrorOf(): Boolean {
    return isFailure && exceptionOrNull() is T
}
