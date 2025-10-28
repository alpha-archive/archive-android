package com.alpha.archiveandroid.core.network

/**
 * 앱 전체에서 사용하는 통일된 에러 타입
 * HTTP 코드를 도메인 에러로 매핑
 */
sealed class AppError(
    override val message: String,
    open val originalException: Throwable? = null
) : Throwable(message) {
    data class Network(
        override val message: String,
        override val originalException: Throwable? = null
    ) : AppError(message, originalException)
    
    data class Unauthorized(
        override val message: String = "인증이 필요합니다",
        override val originalException: Throwable? = null
    ) : AppError(message, originalException)
    
    data class Server(
        override val message: String = "서버 오류가 발생했습니다",
        override val originalException: Throwable? = null
    ) : AppError(message, originalException)
    
    data class Parsing(
        override val message: String = "데이터 파싱 오류가 발생했습니다",
        override val originalException: Throwable? = null
    ) : AppError(message, originalException)
    
    data class Unknown(
        override val message: String = "알 수 없는 오류가 발생했습니다",
        override val originalException: Throwable? = null
    ) : AppError(message, originalException)
}
