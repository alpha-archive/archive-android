package com.alpha.archiveandroid.core.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 날짜 포맷팅 유틸리티
 */
object DateFormatter {
    
    // API에서 받는 날짜 형식들
    private const val ISO_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    private const val ISO_DATE_TIME_FORMAT_NO_MS = "yyyy-MM-dd'T'HH:mm:ss'Z'"
    private const val ISO_DATE_FORMAT = "yyyy-MM-dd"
    
    // 표시할 날짜 형식들
    private const val DISPLAY_DATE_FORMAT = "yyyy-MM-dd"
    private const val DISPLAY_DATE_RANGE_FORMAT = "yyyy/M/d"
    
    /**
     * ISO 날짜 문자열을 yyyy-MM-dd 형식으로 변환
     */
    fun formatToDateString(isoDateString: String): String {
        return try {
            val date = parseISODate(isoDateString)
            val formatter = SimpleDateFormat(DISPLAY_DATE_FORMAT, Locale.getDefault())
            formatter.format(date)
        } catch (e: Exception) {
            // 파싱 실패 시 원본 문자열 반환
            isoDateString
        }
    }
    
    /**
     * 날짜 범위를 yyyy/M/d ~ yyyy/M/d 형식으로 변환
     */
    fun formatDateRange(startDateString: String, endDateString: String): String {
        return try {
            val startDate = parseISODate(startDateString)
            val endDate = parseISODate(endDateString)
            val formatter = SimpleDateFormat(DISPLAY_DATE_RANGE_FORMAT, Locale.getDefault())
            val startStr = formatter.format(startDate)
            val endStr = formatter.format(endDate)
            "$startStr ~ $endStr"
        } catch (e: Exception) {
            // 파싱 실패 시 원본 문자열 반환
            "$startDateString ~ $endDateString"
        }
    }
    
    /**
     * yyyy-MM-dd 또는 yyyy-MM-ddTHH:mm:ss 형식을 yyyy/M/d 형식으로 변환
     * T 이후의 시간 부분은 제거됨 (parseISODate 재사용)
     */
    fun formatToDisplayDate(dateString: String): String {
        return try {
            val date = parseISODate(dateString)
            val formatter = SimpleDateFormat(DISPLAY_DATE_RANGE_FORMAT, Locale.getDefault())
            formatter.format(date)
        } catch (e: Exception) {
            // 파싱 실패 시 T 이후만 제거해서 반환
            extractDateOnly(dateString)
        }
    }
    
    /**
     * yyyy-MM-dd 또는 yyyy-MM-ddTHH:mm:ss 형식에서 yyyy-MM-dd만 추출
     * T 이후의 시간 부분은 제거됨
     */
    fun extractDateOnly(dateString: String): String {
        return if (dateString.contains("T")) {
            dateString.substringBefore("T")
        } else {
            dateString
        }
    }
    
    /**
     * ISO 날짜 문자열을 Date 객체로 파싱
     */
    private fun parseISODate(dateString: String): Date {
        return try {
            // 먼저 밀리초가 있는 형식으로 시도
            SimpleDateFormat(ISO_DATE_TIME_FORMAT, Locale.getDefault()).parse(dateString)
        } catch (e: Exception) {
            try {
                // 밀리초가 없는 형식으로 시도
                SimpleDateFormat(ISO_DATE_TIME_FORMAT_NO_MS, Locale.getDefault()).parse(dateString)
            } catch (e: Exception) {
                // 날짜만 있는 형식으로 시도
                SimpleDateFormat(ISO_DATE_FORMAT, Locale.getDefault()).parse(dateString)
            }
        } ?: throw IllegalArgumentException("Unable to parse date: $dateString")
    }
}