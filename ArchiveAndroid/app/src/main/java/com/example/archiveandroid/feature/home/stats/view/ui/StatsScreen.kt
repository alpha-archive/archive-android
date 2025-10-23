package com.example.archiveandroid.feature.home.stats.view.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.archiveandroid.core.ui.components.TopAppBar
import com.example.archiveandroid.feature.home.stats.data.SampleStatsData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen() {
    val statsData = SampleStatsData.getStatsData()
    var isWeekly by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = "나의 통계",
                showBack = false
            )
        },
        contentWindowInsets = WindowInsets(0)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // 전체 기록 통계
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                StatsCard(
                    title = "활동 기록",
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        StatItem(value = "${statsData.totalRecords}")
                    }
                }
            }

            // 날짜별 통계 (주간/월간 토글)
            StatsCard(
                title = "날짜별 통계",
                topEndAction = {
                    CustomToggle(
                        isWeekly = isWeekly,
                        onToggleChange = { isWeekly = it }
                    )
                }
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // 차트 표시
                if (isWeekly) {
                    DailyChart(dailyData = statsData.dailyData)
                } else {
                    MonthlyCalendar(calendarData = statsData.calendarData)
                }
            }

            // 활동 유형별 통계
            StatsCard(title = "활동 유형별 통계") {
                Spacer(modifier = Modifier.height(8.dp))
                ActivityTypeChart(activityTypes = statsData.activityTypes)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun StatItem(value: String) {
    Row {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.alignByBaseline()
        )
        Text(
            text = "개",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.alignByBaseline()
        )
    }
}
