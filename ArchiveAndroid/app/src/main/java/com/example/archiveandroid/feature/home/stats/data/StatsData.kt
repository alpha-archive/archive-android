package com.example.archiveandroid.feature.home.stats.data

import androidx.compose.ui.graphics.Color
import com.example.archiveandroid.core.util.CategoryColorGenerator

data class DailyData(
    val day: String,
    val count: Int
)

data class MonthlyData(
    val month: String,
    val count: Int
)

data class CalendarDayData(
    val day: Int,
    val hasActivity: Boolean,
    val activityCount: Int = 0
)

data class ActivityTypeData(
    val type: String,
    val count: Int,
    val color: Color
)

data class RecentActivityData(
    val type: String,
    val date: String
)

data class StatsData(
    val totalRecords: Int,
    val totalHours: Int,
    val dailyData: List<DailyData>,
    val monthlyData: List<MonthlyData>,
    val calendarData: List<CalendarDayData>,
    val activityTypes: List<ActivityTypeData>,
    val recentActivities: List<RecentActivityData>
)

object SampleStatsData {
    fun getStatsData(): StatsData {
        return StatsData(
            totalRecords = 24,
            totalHours = 48,
            dailyData = listOf(
                DailyData("월", 8),
                DailyData("화", 12),
                DailyData("수", 6),
                DailyData("목", 15),
                DailyData("금", 10),
                DailyData("토", 18),
                DailyData("일", 14)
            ),
            monthlyData = listOf(
                MonthlyData("1월", 8),
                MonthlyData("2월", 12),
                MonthlyData("3월", 6),
                MonthlyData("4월", 15),
                MonthlyData("5월", 10),
                MonthlyData("6월", 18)
            ),
            calendarData = listOf(
                CalendarDayData(1, true, 1),  // 약한 보라색
                CalendarDayData(2, false),
                CalendarDayData(3, true, 2),  // 약간 더 진한 보라색
                CalendarDayData(4, false),
                CalendarDayData(5, false),
                CalendarDayData(6, false),
                CalendarDayData(7, true, 3),  // 보통 보라색
                CalendarDayData(8, false),
                CalendarDayData(9, false),
                CalendarDayData(10, false),
                CalendarDayData(11, true, 4),  // 진한 보라색
                CalendarDayData(12, true, 1),  // 약한 보라색
                CalendarDayData(13, false),
                CalendarDayData(14, false),
                CalendarDayData(15, false),
                CalendarDayData(16, false),
                CalendarDayData(17, false),
                CalendarDayData(18, true, 2),  // 약간 더 진한 보라색
                CalendarDayData(19, true, 1),  // 약한 보라색
                CalendarDayData(20, false),
                CalendarDayData(21, false),
                CalendarDayData(22, true, 3),  // 보통 보라색
                CalendarDayData(23, false),
                CalendarDayData(24, true, 5),  // 진한 보라색
                CalendarDayData(25, true, 4),  // 진한 보라색
                CalendarDayData(26, false),
                CalendarDayData(27, true, 2),  // 약간 더 진한 보라색
                CalendarDayData(28, false),
                CalendarDayData(29, false),
                CalendarDayData(30, false),
                CalendarDayData(31, false)
            ),
            activityTypes = listOf(
                ActivityTypeData("운동", 8, CategoryColorGenerator.getCategoryColors("운동").second),
                ActivityTypeData("문화", 6, CategoryColorGenerator.getCategoryColors("문화").second),
                ActivityTypeData("여행", 4, CategoryColorGenerator.getCategoryColors("여행").second),
                ActivityTypeData("기타", 6, CategoryColorGenerator.getCategoryColors("기타").second)
            ),
            recentActivities = listOf(
                RecentActivityData("운동", "2023-10-26"),
                RecentActivityData("독서", "2023-10-25"),
                RecentActivityData("여행", "2023-10-24")
            )
        )
    }
}
