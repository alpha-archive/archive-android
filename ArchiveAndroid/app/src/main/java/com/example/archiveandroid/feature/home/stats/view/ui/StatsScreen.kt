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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.archiveandroid.core.ui.components.TopAppBar
import com.example.archiveandroid.feature.home.stats.data.SampleStatsData
import com.example.archiveandroid.feature.home.stats.view.StatsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(
    viewModel: StatsViewModel = hiltViewModel()
) {
    val statsData = SampleStatsData.getStatsData()
    var isWeekly by remember { mutableStateOf(true) }
    
    val totalActivities by viewModel.totalActivities.collectAsStateWithLifecycle()
    val categoryStats by viewModel.categoryStats.collectAsStateWithLifecycle()
    val dailyStats by viewModel.dailyStats.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = "나의 통계",
                showBack = false
            )
        },
        contentWindowInsets = WindowInsets(0)
    ) { paddingValues ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (error != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = error ?: "오류가 발생했습니다",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
        } else {
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
                            StatItem(value = "$totalActivities")
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
                        if (dailyStats.isNotEmpty()) {
                            DailyChart(dailyData = dailyStats)
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "주간 통계 데이터가 없습니다",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    } else {
                        MonthlyCalendar(calendarData = statsData.calendarData)
                    }
                }

                // 활동 유형별 통계
                StatsCard(title = "활동 유형별 통계") {
                    Spacer(modifier = Modifier.height(8.dp))
                    if (categoryStats.isNotEmpty()) {
                        ActivityTypeChart(activityTypes = categoryStats)
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "활동 유형별 통계가 없습니다",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
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
