package com.alpha.archive.core.util

import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase

/**
 * Crashlytics에 non-fatal 에러를 로깅하는 유틸리티
 * 
 * 주로 toUserFriendlyMessage()를 사용하지 않는 하드코딩된 에러 메시지 케이스에서 사용
 */
object CrashlyticsLogger {
    
    /**
     * Throwable 로깅 (컨텍스트와 함께)
     */
    fun logException(throwable: Throwable, context: String? = null) {
        context?.let { Firebase.crashlytics.log(it) }
        Firebase.crashlytics.recordException(throwable)
    }
}
