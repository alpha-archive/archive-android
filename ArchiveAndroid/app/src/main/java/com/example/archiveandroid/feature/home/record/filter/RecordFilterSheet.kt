package com.example.archiveandroid.feature.home.record.filter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.clickable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalConfiguration

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordFilterSheet(
    onDismiss: () -> Unit,
    onFiltersApplied: (Set<String>) -> Unit,
    selectedFilters: Set<String>,
    onDone: (() -> Unit)? = null,
    viewModel: RecordFilterViewModel = hiltViewModel()
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    val uiState by viewModel.uiState.collectAsState()

    val screenHeight = LocalConfiguration.current.screenHeightDp
    val maxSheetHeight = (screenHeight * 0.85f).dp

    // 필터 화면이 열릴 때마다 카테고리 다시 로드
    LaunchedEffect(Unit) {
        viewModel.loadCategories()
        if (sheetState.hasPartiallyExpandedState) {
            sheetState.partialExpand()
        }
    }

    // 선택된 필터 상태를 초기화
    LaunchedEffect(selectedFilters) {
        // RecordFilterViewModel의 상태를 selectedFilters로 초기화
        viewModel.initializeWithSelectedFilters(selectedFilters)
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White
    ) {
        Column(modifier = Modifier.padding(bottom = 16.dp).heightIn(max = maxSheetHeight)) {
            // Header: centered title (clickable to dismiss) and right-aligned Done
            androidx.compose.foundation.layout.Box(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 12.dp)
            ) {
                // Done aligned to end
                Text(
                    text = "완료",
                    color = Color(0xFF2196F3),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.align(Alignment.CenterEnd).padding(end = 16.dp).clickable { 
                        val selectedIds = uiState.options.filter { it.selected }.map { it.id }.toSet()
                        onFiltersApplied(selectedIds)
                        onDone?.invoke()
                    }
                )
                // Title centered, single line
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.align(Alignment.Center).clickable { onDismiss() }
                ) {
                    Text(
                        text = "카테고리 값을 포함하는 데이터",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.Center,
                        maxLines = 1
                    )
                    Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = null, tint = Color.Gray)
                }
            }
            // Selected chips summary (horizontally scrollable)
            SelectedSummary(uiState, onRemove = { id -> viewModel.toggle(id) })

            Divider()
            LazyColumn(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
                items(uiState.options) { opt ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Checkbox(checked = opt.selected, onCheckedChange = { viewModel.toggle(opt.id) })
                        LabelChip(text = opt.label, bg = opt.bgColor, fg = opt.textColor)
                    }
                }
            }
        }
    }
}

@Composable
private fun LabelChip(text: String, bg: Color, fg: Color) {
    Text(
        text = text,
        color = fg,
        style = MaterialTheme.typography.bodyMedium.copy(
            fontWeight = FontWeight.Medium
        ),
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bg)
            .padding(horizontal = 10.dp, vertical = 6.dp)
    )
}

@Composable
private fun SelectedSummary(state: RecordFilterUiState, onRemove: (String) -> Unit) {
    val selected = state.options.filter { it.selected }
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
        if (selected.isEmpty()) {
            Text(
                text = "하나 이상의 옵션을 선택하세요.",
                color = Color.Gray,
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                selected.forEach { opt ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(opt.bgColor)
                            .clickable { onRemove(opt.id) }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = opt.label,
                            color = opt.textColor,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Medium
                            )
                        )
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = null,
                            tint = opt.textColor,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }
            }
        }
    }
}


