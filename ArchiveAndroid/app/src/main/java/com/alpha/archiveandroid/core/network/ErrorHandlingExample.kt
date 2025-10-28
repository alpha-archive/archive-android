package com.alpha.archiveandroid.core.network

/**
 * AppError 사용 예시
 */
class ErrorHandlingExample {
    
    fun handleError(result: Result<String>) {
        result.fold(
            onSuccess = { data ->
                // 성공 처리
                println("Success: $data")
            },
            onFailure = { error ->
                when (error) {
                    is AppError.Network -> {
                        // 네트워크 오류 처리
                        println("네트워크 오류: ${error.message}")
                        // UI에 "네트워크 연결을 확인해주세요" 표시
                    }
                    is AppError.Unauthorized -> {
                        // 인증 오류 처리
                        println("인증 오류: ${error.message}")
                        // 로그인 화면으로 이동
                    }
                    is AppError.Server -> {
                        // 서버 오류 처리
                        println("서버 오류: ${error.message}")
                        // "잠시 후 다시 시도해주세요" 표시
                    }
                    is AppError.Parsing -> {
                        // 파싱 오류 처리
                        println("파싱 오류: ${error.message}")
                        // "데이터 형식 오류" 표시
                    }
                    is AppError.Unknown -> {
                        // 알 수 없는 오류 처리
                        println("알 수 없는 오류: ${error.message}")
                        // "알 수 없는 오류가 발생했습니다" 표시
                    }
                    else -> {
                        // 기타 Exception 처리
                        println("기타 오류: ${error.message}")
                    }
                }
            }
        )
    }
}
