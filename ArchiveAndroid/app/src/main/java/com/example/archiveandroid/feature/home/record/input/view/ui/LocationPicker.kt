package com.example.archiveandroid.feature.home.record.input.view.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.archiveandroid.feature.home.record.input.data.remote.dto.Place
import com.example.archiveandroid.feature.home.record.input.viewmodel.PlaceSearchViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationPicker(
    selectedPlace: Place?,
    onPlaceSelected: (Place) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PlaceSearchViewModel = hiltViewModel()
) {
    var showSearchDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    
    val uiState by viewModel.uiState.collectAsState()
    
    val displayText = selectedPlace?.let { 
        if (it.address.isNotEmpty()) "${it.name} (${it.address})" else it.name
    } ?: "장소를 선택하세요"
    
    // 검색어가 변경될 때마다 API 호출
    LaunchedEffect(searchQuery) {
        if (searchQuery.isNotBlank()) {
            viewModel.searchPlaces(searchQuery)
        }
    }
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { showSearchDialog = true },
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
                color = if (selectedPlace != null) Color(0xFF646464) else Color(0xFF898989),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Light
                ),
                modifier = Modifier.weight(1f)
            )
            
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "장소 선택",
                tint = Color(0xFF646464),
                modifier = Modifier.size(20.dp)
            )
        }
    }
    
    // 장소 검색 다이얼로그
    if (showSearchDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showSearchDialog = false },
            title = {
                Text(
                    text = "장소 검색",
                    style = MaterialTheme.typography.titleLarge
                )
            },
            text = {
                Column {
                    // 검색 입력 필드
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { query ->
                            searchQuery = query
                        },
                        placeholder = { 
                            Text(
                                text = "장소명을 입력하세요",
                                style = MaterialTheme.typography.bodyMedium
                            ) 
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "검색"
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    
                    Spacer(modifier = Modifier.padding(16.dp))
                    
                    // 로딩 상태
                    if (uiState.isLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    // 에러 상태 - 사용자 입력 텍스트를 주소로 사용
                    else if (uiState.error != null && searchQuery.isNotBlank()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "검색 중 오류가 발생했습니다.",
                                    color = Color.Red,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                androidx.compose.material3.TextButton(
                                    onClick = {
                                        val place = Place(
                                            id = "manual_${System.currentTimeMillis()}",
                                            name = searchQuery,
                                            category = "기타",
                                            phone = "",
                                            address = searchQuery,
                                            roadAddress = searchQuery,
                                            longitude = "",
                                            latitude = "",
                                            distance = ""
                                        )
                                        onPlaceSelected(place)
                                        showSearchDialog = false
                                    }
                                ) {
                                    Text(
                                        text = "\"$searchQuery\"를 주소로 사용",
                                        color = Color(0xFF646464),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }
                    // 검색 결과
                    else if (uiState.searchResults.isNotEmpty()) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                        ) {
                            items(uiState.searchResults) { place ->
                                PlaceItem(
                                    place = place,
                                    onClick = {
                                        onPlaceSelected(place)
                                        showSearchDialog = false
                                    }
                                )
                                if (place != uiState.searchResults.last()) {
                                    Divider()
                                }
                            }
                        }
                    } else if (searchQuery.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "검색 결과가 없습니다",
                                color = Color(0xFF898989),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            },
            confirmButton = {
                androidx.compose.material3.TextButton(
                    onClick = { showSearchDialog = false }
                ) {
                    Text(
                        text = "취소",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        )
    }
}

@Composable
private fun PlaceItem(
    place: Place,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = null,
            tint = Color(0xFF646464),
            modifier = Modifier.size(20.dp)
        )
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = place.name,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = Color(0xFF333333)
            )
            Text(
                text = place.address,
                color = Color(0xFF898989),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = place.category,
                color = Color(0xFF646464),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
