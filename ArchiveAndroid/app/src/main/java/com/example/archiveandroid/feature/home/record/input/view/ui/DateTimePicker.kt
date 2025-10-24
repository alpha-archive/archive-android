package com.example.archiveandroid.feature.home.record.input.view.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimePicker(
    selectedDateTime: Date?,
    onDateTimeSelected: (Date) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDatePicker by remember { mutableStateOf(false) }
    
    val dateFormatter = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREAN)
    
    val displayText = if (selectedDateTime != null) {
        dateFormatter.format(selectedDateTime)
    } else {
        "날짜를 선택하세요"
    }
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { showDatePicker = true },
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = displayText,
                color = if (selectedDateTime != null) Color(0xFF646464) else Color(0xFF898989),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Light
                )
            )
            
            Icon(
                imageVector = Icons.Default.CalendarToday,
                contentDescription = "날짜 선택",
                tint = Color(0xFF646464),
                modifier = Modifier.width(20.dp)
            )
        }
    }
    
    // Date Picker Dialog
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDateTime?.time ?: System.currentTimeMillis()
        )
        
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                Button(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            onDateTimeSelected(Date(millis))
                            showDatePicker = false
                        }
                    }
                ) {
                    Text(
                        text = "확인",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDatePicker = false }
                ) {
                    Text(
                        text = "취소",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
