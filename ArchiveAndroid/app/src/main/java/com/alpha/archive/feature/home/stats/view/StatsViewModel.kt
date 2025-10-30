package com.alpha.archive.feature.home.stats.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alpha.archive.core.network.toUserFriendlyMessage
import com.alpha.archive.core.util.CategoryColorGenerator
import com.alpha.archive.feature.home.stats.data.ActivityTypeData
import com.alpha.archive.feature.home.stats.data.CalendarDayData
import com.alpha.archive.feature.home.stats.data.DailyData
import com.alpha.archive.feature.home.stats.data.repository.StatsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val statsRepository: StatsRepository
) : ViewModel() {

    private val _totalActivities = MutableStateFlow(0)
    val totalActivities: StateFlow<Int> = _totalActivities.asStateFlow()

    private val _categoryStats = MutableStateFlow<List<ActivityTypeData>>(emptyList())
    val categoryStats: StateFlow<List<ActivityTypeData>> = _categoryStats.asStateFlow()

    private val _dailyStats = MutableStateFlow<List<DailyData>>(emptyList())
    val dailyStats: StateFlow<List<DailyData>> = _dailyStats.asStateFlow()

    private val _monthlyCalendarData = MutableStateFlow<List<CalendarDayData>>(emptyList())
    val monthlyCalendarData: StateFlow<List<CalendarDayData>> = _monthlyCalendarData.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private var currentYearMonth: String = ""

    init {
        loadStatistics()
        // 초기 월간 통계 로드 (현재 월)
        val calendar = Calendar.getInstance()
        currentYearMonth = String.format("%04d-%02d", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1)
        loadMonthlyStatistics(currentYearMonth)
    }

    fun refreshStatistics() {
        loadStatistics()
        // 현재 보고 있던 월 다시 로드
        if (currentYearMonth.isNotEmpty()) {
            loadMonthlyStatistics(currentYearMonth)
        }
    }

    fun loadStatistics() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            // 전체 통계와 주간 통계를 동시에 로드
            val overallResult = statsRepository.getOverallStatistics()
            val weeklyResult = statsRepository.getWeeklyStatistics()

            overallResult
                .onSuccess { response ->
                    _totalActivities.value = response.totalActivities
                    _categoryStats.value = response.categoryStats.map { stat ->
                        val (bgColor, _) = CategoryColorGenerator.getCategoryColors(stat.displayName)
                        ActivityTypeData(
                            type = stat.displayName,
                            count = stat.count,
                            color = bgColor
                        )
                    }
                }
                .onFailure { throwable ->
                    _error.value = throwable.toUserFriendlyMessage()
                }

            weeklyResult
                .onSuccess { response ->
                    val dailyData = response.dailyStats.map { stat ->
                        DailyData(
                            day = stat.dayOfWeek,
                            count = stat.count
                        )
                    }
                    _dailyStats.value = reorderDailyDataToEndWithToday(dailyData)
                }
                .onFailure { throwable ->
                    if (_error.value == null) {
                        _error.value = throwable.toUserFriendlyMessage()
                    }
                }

            _isLoading.value = false
        }
    }

    private fun reorderDailyDataToEndWithToday(dailyData: List<DailyData>): List<DailyData> {
        // 요일 순서 정의
        val dayOrder = listOf("월", "화", "수", "목", "금", "토", "일")
        
        // 오늘 요일 구하기 (Calendar.SUNDAY = 1, MONDAY = 2, ...)
        val calendar = Calendar.getInstance()
        val todayIndex = when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> 0
            Calendar.TUESDAY -> 1
            Calendar.WEDNESDAY -> 2
            Calendar.THURSDAY -> 3
            Calendar.FRIDAY -> 4
            Calendar.SATURDAY -> 5
            Calendar.SUNDAY -> 6
            else -> 0
        }
        
        // 오늘부터 역순으로 7일 순서 만들기 (오늘이 가장 오른쪽)
        val reorderedDays = mutableListOf<String>()
        for (i in 6 downTo 0) {
            val dayIndex = (todayIndex - i + 7) % 7
            reorderedDays.add(dayOrder[dayIndex])
        }
        
        // 데이터를 Map으로 변환
        val dataMap = dailyData.associateBy { it.day }
        
        // 재정렬된 순서대로 데이터 반환 (없는 요일은 count 0으로)
        return reorderedDays.map { day ->
            dataMap[day] ?: DailyData(day = day, count = 0)
        }
    }

    fun loadMonthlyStatistics(yearMonth: String) {
        currentYearMonth = yearMonth
        viewModelScope.launch {
            statsRepository.getMonthlyStatistics(yearMonth)
                .onSuccess { response ->
                    // API 응답의 dailyActivities를 CalendarDayData로 변환
                    _monthlyCalendarData.value = response.dailyActivities.map { activity ->
                        // day 형식: "2025-10-01" -> 일자만 추출
                        val dayNumber = activity.day.split("-").lastOrNull()?.toIntOrNull() ?: 1
                        CalendarDayData(
                            day = dayNumber,
                            hasActivity = activity.count > 0,
                            activityCount = activity.count
                        )
                    }
                }
                .onFailure { throwable ->
                    // 에러는 로그만 남기고 빈 데이터로 유지
                    _monthlyCalendarData.value = emptyList()
                }
        }
    }

}

