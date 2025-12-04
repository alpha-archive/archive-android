package com.alpha.archive.core.network

import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
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

/**
 * Exception을 사용자 친화적인 메시지로 변환
 * 배포 환경에서 상세 로그가 노출되는 것을 방지
 * 
 * @param logToCrashlytics Crashlytics에 non-fatal 에러로 기록할지 여부 (기본값: true)
 */
fun Throwable.toUserFriendlyMessage(logToCrashlytics: Boolean = true): String {
    // Crashlytics에 non-fatal 에러 기록
    if (logToCrashlytics) {
        Firebase.crashlytics.recordException(this)
    }
    
    return when (this) {
        is AppError.Network -> "인터넷 연결이 불안정합니다"
        is AppError.Unauthorized -> "로그인이 필요합니다"
        is AppError.Server -> "서버에 일시적인 문제가 발생했습니다"
        is AppError.Parsing -> "데이터를 처리할 수 없습니다"
        is AppError.Unknown -> "일시적인 오류가 발생했습니다"
        is java.net.UnknownHostException,
        is java.net.SocketTimeoutException,
        is java.net.ConnectException,
        is java.io.IOException -> "인터넷 연결이 불안정합니다"
        else -> "일시적인 오류가 발생했습니다"
    }
}
