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
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.archiveandroid.feature.home.record.input.data.remote.dto.PlaceDocument
import com.example.archiveandroid.feature.home.record.input.viewmodel.PlaceSearchViewModel

data class Place(
    val id: String,
    val name: String,
    val address: String,
    val category: String? = null
)

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
    
    val searchResults by viewModel.searchResults.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    
    val displayText = selectedPlace?.let { "${it.name} (${it.address})" } ?: "장소를 선택하세요"
    
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
                fontWeight = FontWeight.Light,
                fontSize = 14.sp,
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
                        placeholder = { Text("장소명을 입력하세요") },
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
                    if (isLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    // 에러 상태
                    else if (error != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = error.toString(),
                                color = Color.Red,
                                fontSize = 14.sp
                            )
                        }
                    }
                    // 검색 결과
                    else if (searchResults.isNotEmpty()) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                        ) {
                            items(searchResults) { document ->
                                PlaceDocumentItem(
                                    document = document,
                                    onClick = {
                                        val place = Place(
                                            id = document.id,
                                            name = document.placeName,
                                            address = document.addressName,
                                            category = document.categoryName
                                        )
                                        onPlaceSelected(place)
                                        showSearchDialog = false
                                    }
                                )
                                if (document != searchResults.last()) {
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
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            },
            confirmButton = {
                androidx.compose.material3.TextButton(
                    onClick = { showSearchDialog = false }
                ) {
                    Text("취소")
                }
            }
        )
    }
}

@Composable
private fun PlaceDocumentItem(
    document: PlaceDocument,
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
                text = document.placeName,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = Color(0xFF333333)
            )
            Text(
                text = document.addressName,
                fontSize = 12.sp,
                color = Color(0xFF898989)
            )
            Text(
                text = document.categoryName,
                fontSize = 11.sp,
                color = Color(0xFF646464)
            )
        }
    }
}
