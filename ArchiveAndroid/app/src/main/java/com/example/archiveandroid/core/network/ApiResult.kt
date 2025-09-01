package com.example.archiveandroid.core.network

/**
 * API 호출 결과를 래핑하는 공통 클래스
 * 성공/실패 상태를 명확하게 구분하고 에러 정보를 포함
 */
sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(val message: String, val code: Int? = null) : ApiResult<Nothing>()
    data object Loading : ApiResult<Nothing>()
}

/**
 * 네트워크 에러 타입 정의
 */
sealed class NetworkError : Exception() {
    data class HttpError(val code: Int, override val message: String) : NetworkError()
    data class ConnectionError(override val message: String) : NetworkError()
    data class ParseError(override val message: String) : NetworkError()
    data class UnknownError(override val message: String) : NetworkError()
}
