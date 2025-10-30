package com.alpha.archive.feature.home.stats.view.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.alpha.archive.feature.home.stats.data.CalendarDayData
import java.util.Calendar

@Composable
fun MonthlyCalendar(
    calendarData: List<CalendarDayData>,
    onMonthChange: (String) -> Unit = {}
) {
    // 현재 날짜를 기본값으로 사용
    val today = remember {
        Calendar.getInstance()
    }
    
    var currentYear by remember { mutableStateOf(today.get(Calendar.YEAR)) }
    var currentMonth by remember { mutableStateOf(today.get(Calendar.MONTH) + 1) } // Calendar.MONTH는 0부터 시작
    var selectedDay by remember { mutableStateOf<Int?>(null) }
    
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // 월/년도 헤더
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                if (currentMonth == 1) {
                    currentMonth = 12
                    currentYear -= 1
                } else {
                    currentMonth -= 1
                }
                // 월 변경 시 선택 초기화 및 콜백 호출
                selectedDay = null
                val yearMonth = String.format("%04d-%02d", currentYear, currentMonth)
                onMonthChange(yearMonth)
            }) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "이전 달",
                    tint = Color(0xFF666666)
                )
            }
            
            Text(
                text = "${currentYear}년 ${currentMonth}월",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            
            IconButton(onClick = {
                if (currentMonth == 12) {
                    currentMonth = 1
                    currentYear += 1
                } else {
                    currentMonth += 1
                }
                // 월 변경 시 선택 초기화 및 콜백 호출
                selectedDay = null
                val yearMonth = String.format("%04d-%02d", currentYear, currentMonth)
                onMonthChange(yearMonth)
            }) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "다음 달",
                    tint = Color(0xFF666666)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // 요일 헤더
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf("일", "월", "화", "수", "목", "금", "토").forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF999999),
                    fontSize = 14.sp
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 날짜 그리드 (5주 가정)
        val weeks = getWeeksForMonth(currentYear, currentMonth, calendarData)
        
        weeks.forEach { week ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                week.forEach { dayData ->
                    CalendarDayCell(
                        dayData = dayData,
                        isSelected = dayData?.day == selectedDay,
                        onDayClick = { day ->
                            selectedDay = if (selectedDay == day) null else day
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun CalendarDayCell(
    dayData: CalendarDayData?,
    isSelected: Boolean = false,
    onDayClick: (Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .padding(2.dp),
        contentAlignment = Alignment.Center
    ) {
        if (dayData != null) {
            // 활동 갯수에 따른 색상 결정
            val backgroundColor = when {
                !dayData.hasActivity -> Color.Transparent
                dayData.activityCount == 1 -> Color(0xFFE1BEE7) // 약한 보라색
                dayData.activityCount == 2 -> Color(0xFFCE93D8) // 약간 더 진한 보라색
                dayData.activityCount == 3 -> Color(0xFFBA68C8) // 보통 보라색
                else -> Color(0xFF9C27B0) // 진한 보라색 (4개 이상)
            }
            
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = backgroundColor,
                        shape = CircleShape
                    )
                    .then(
                        if (isSelected) {
                            Modifier.border(
                                width = 2.dp,
                                color = Color(0xFF6200EA),
                                shape = CircleShape
                            )
                        } else Modifier
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        onDayClick(dayData.day)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = dayData.day.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF333333),
                    fontWeight = if (dayData.hasActivity || isSelected) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 16.sp
                )
            }
            
            // 개수 툴팁 (선택 시만 표시) - 절대 위치로 날짜 위에 배치
            AnimatedVisibility(
                visible = isSelected && dayData.hasActivity,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = (-28).dp)
                    .zIndex(10f)
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            Color(0xFF333333),
                            RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "${dayData.activityCount}개",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

// 월의 주차별 날짜 데이터를 생성하는 헬퍼 함수
private fun getWeeksForMonth(
    year: Int,
    month: Int,
    calendarData: List<CalendarDayData>
): List<List<CalendarDayData?>> {
    val weeks = mutableListOf<List<CalendarDayData?>>()
    
    // Calendar를 사용하여 해당 월의 정보 계산
    val calendar = Calendar.getInstance().apply {
        set(Calendar.YEAR, year)
        set(Calendar.MONTH, month - 1) // Calendar.MONTH는 0부터 시작 (0=1월)
        set(Calendar.DAY_OF_MONTH, 1)
    }
    
    // 해당 월의 첫날 요일 (1=일요일, 2=월요일, ..., 7=토요일)
    val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1 // 0=일요일로 변환
    
    // 해당 월의 일수
    val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    
    var currentWeek = mutableListOf<CalendarDayData?>()
    
    // 첫 주의 빈 칸 채우기
    repeat(firstDayOfWeek) {
        currentWeek.add(null)
    }
    
    // 날짜 채우기
    for (day in 1..daysInMonth) {
        val dayData = calendarData.find { it.day == day }
        currentWeek.add(dayData ?: CalendarDayData(day, false))
        
        if (currentWeek.size == 7) {
            weeks.add(currentWeek.toList())
            currentWeek.clear()
        }
    }
    
    // 마지막 주의 빈 칸 채우기
    if (currentWeek.isNotEmpty()) {
        while (currentWeek.size < 7) {
            currentWeek.add(null)
        }
        weeks.add(currentWeek.toList())
    }
    
    return weeks
}

