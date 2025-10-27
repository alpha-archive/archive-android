package com.example.archiveandroid.feature.home.record.input

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults.colors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.archiveandroid.core.ui.components.TopAppBar
import com.example.archiveandroid.feature.home.record.input.data.remote.dto.ImageUploadData
import com.example.archiveandroid.feature.home.record.input.data.remote.dto.Place
import com.example.archiveandroid.feature.home.record.input.data.remote.dto.RecordInputRequest
import com.example.archiveandroid.feature.home.record.input.view.ui.DateTimePicker
import com.example.archiveandroid.feature.home.record.input.view.ui.LocationPicker
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordInputScreen(
    ui: RecordInputUiState,
    onBack: () -> Unit,
    onSave: (RecordInputRequest) -> Unit,
    onImageUpload: (File) -> Unit,
    onImageRemove: (String) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    var selectedDateTime by remember { mutableStateOf<Date?>(Date()) }
    var selectedPlace by remember { mutableStateOf<Place?>(null) }
    var draft by remember { 
        mutableStateOf(
            ui.draft ?: RecordDraft(
                activityDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            )
        ) 
    }

    // 수정 모드일 때 기존 데이터 로드
    LaunchedEffect(ui.draft) {
        ui.draft?.let { loadedDraft ->
            draft = loadedDraft
            
            // 기존 장소 정보 로드
            if (loadedDraft.location.isNotEmpty()) {
                selectedPlace = Place(
                    id = "",
                    name = loadedDraft.location,
                    address = "",
                    roadAddress = "",
                    category = "",
                    phone = "",
                    longitude = "",
                    latitude = "",
                    distance = ""
                )
            }
            
            // 기존 날짜 정보 로드
            loadedDraft.activityDate?.let { dateStr ->
                try {
                    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    selectedDateTime = formatter.parse(dateStr)
                } catch (e: Exception) {
                    // 파싱 실패 시 현재 날짜 유지
                }
            }
        }
    }

    val categories = listOf(
        "체험", "전시", "뮤지컬", "연극", "영화", "콘서트", 
        "축제", "워크샵", "스포츠", "여행", "독서", "요리",
        "봉사", "취미", "스터디", "네트워킹", "기타"
    )
    
    // 카테고리 표시명 매핑
    val categoryDisplayNames = mapOf(
        "EXPERIENCE" to "체험",
        "EXHIBITION" to "전시", 
        "MUSICAL" to "뮤지컬",
        "THEATER" to "연극",
        "MOVIE" to "영화",
        "CONCERT" to "콘서트",
        "FESTIVAL" to "축제",
        "WORKSHOP" to "워크샵",
        "SPORTS" to "스포츠",
        "TRAVEL" to "여행",
        "READING" to "독서",
        "COOKING" to "요리",
        "VOLUNTEER" to "봉사",
        "HOBBY" to "취미",
        "STUDY" to "스터디",
        "NETWORKING" to "네트워킹",
        "ETC" to "기타"
    )
    
    // 카테고리 표시명을 실제 값으로 변환하는 매핑
    val categoryValues = mapOf(
        "체험" to "EXPERIENCE",
        "전시" to "EXHIBITION",
        "뮤지컬" to "MUSICAL", 
        "연극" to "THEATER",
        "영화" to "MOVIE",
        "콘서트" to "CONCERT",
        "축제" to "FESTIVAL",
        "워크샵" to "WORKSHOP",
        "스포츠" to "SPORTS",
        "여행" to "TRAVEL",
        "독서" to "READING",
        "요리" to "COOKING",
        "봉사" to "VOLUNTEER",
        "취미" to "HOBBY",
        "스터디" to "STUDY",
        "네트워킹" to "NETWORKING",
        "기타" to "ETC"
    )

    val textfieldColors = colors(
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        focusedTextColor = Color(0xFF646464),
        unfocusedTextColor = Color(0xFF646464),
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        errorIndicatorColor = Color.Transparent
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = "",
                showBack = true,
                onBackClick = onBack,
                scrollBehavior = scrollBehavior,
                actions = {}
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp)
            ) {
                Button(
                    onClick = {
                           val req = RecordInputRequest(
                               title = draft.title,
                               category = draft.category,
                               location = draft.location,
                               activityDate = draft.activityDate?.let { dateStr ->
                                   "${dateStr}T00:00:00.000Z"
                               },
                               rating = draft.rating,
                               memo = draft.memo,
                               imageIds = ui.uploadedImages.map { it.id },
                           )
                           onSave(req)
                    },
                    enabled = !ui.submitting,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "저장",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        contentWindowInsets = WindowInsets(0)
    ) { innerPadding ->
        val scrollState = rememberScrollState()
        
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(top = 20.dp, bottom = 400.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PhotoInput(
                imageUri = imageUri,
                isUploading = ui.isUploadingImage,
                onPick = { uri ->
                    imageUri = uri
                    if (uri != null) {
                        val file = uriToFile(context, uri)
                        onImageUpload(file)
                    }
                },
                uploadedImages = ui.uploadedImages,
                onImageRemove = onImageRemove
            )

            RowInfoInput(label = "카테고리") {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    CategoryInput(
                        categories = categories,
                        selected = categoryDisplayNames[draft.category] ?: draft.category,
                        onSelect = { displayName ->
                            val categoryValue = categoryValues[displayName] ?: displayName
                            draft = draft.copy(category = categoryValue)
                        }
                    )
                }
            }
            Divider(color = Color(0xffD9D9D9), modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp, vertical = 10.dp))

            RowInfoInput(label = "활동명") {
                TextField(
                    value = draft.title,
                    onValueChange = { draft = draft.copy(title = it) },
                    singleLine = true,
                    colors = textfieldColors,
                    modifier = Modifier
                        .widthIn(min = 180.dp, max = 420.dp)
                        .heightIn(min = 44.dp)
                        .fillMaxWidth()
                )
            }
            Divider(color = Color(0xffD9D9D9), modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp, vertical = 10.dp))

            /* 사진에서 날짜/시간, 장소 정보 뽑아서 넣기 */
            RowInfoInput(label = "날짜") {
                Column {
                    DateTimePicker(
                        selectedDateTime = selectedDateTime,
                        onDateTimeSelected = { date ->
                            selectedDateTime = date
                            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            draft = draft.copy(activityDate = formatter.format(date))
                        }
                    )
                }
            }
            Divider(color = Color(0xffD9D9D9), modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp, vertical = 10.dp))

            RowInfoInput(label = "장소") {
                LocationPicker(
                    selectedPlace = selectedPlace,
                    onPlaceSelected = { place ->
                        selectedPlace = place
                        draft = draft.copy(location = place.name)
                    }
                )
            }
            Divider(color = Color(0xffD9D9D9), modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp, vertical = 10.dp))

            RowInfoInput(label = "메모") {
                var previousLength by remember { mutableStateOf(draft.memo.length) }
                
                TextField(
                    value = draft.memo,
                    onValueChange = { newValue ->
                        draft = draft.copy(memo = newValue)
                        // 텍스트가 늘어날 때만 스크롤
                        if (newValue.length > previousLength) {
                            scope.launch {
                                kotlinx.coroutines.delay(50)
                                scrollState.animateScrollTo(scrollState.maxValue)
                            }
                        }
                        previousLength = newValue.length
                    },
                    singleLine = false,
                    minLines = 3,
                    colors = textfieldColors,
                    modifier = Modifier
                        .widthIn(min = 180.dp, max = 420.dp)
                        .heightIn(min = 100.dp)
                        .fillMaxWidth()
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 50.dp),
                contentAlignment = Alignment.BottomCenter
            ) {}
        }
    }
}

data class RecordDraft(
    val imageUri: Uri? = null,
    val category: String = "",
    val title: String = "",
    val memo: String = "",
    val location: String = "",
    val activityDate: String? = null,
    val rating: Int? = null,
    val imageIds: List<String> = emptyList(),
    val publicEventId: String? = null
)

@Composable
private fun PhotoInput(
    imageUri: Uri?,
    isUploading: Boolean,
    onPick: (Uri?) -> Unit,
    uploadedImages: List<ImageUploadData> = emptyList(),
    onImageRemove: (String) -> Unit = {}
) {
    val picker = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri -> onPick(uri) }

    val painter = rememberAsyncImagePainter(model = imageUri)
    val hasImage = imageUri != null || uploadedImages.isNotEmpty()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 50.dp)
    ) {
        // 기존 이미지들이 있으면 LazyRow로 표시
        if (uploadedImages.isNotEmpty()) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                items(uploadedImages) { imageData ->
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(8.dp))
                    ) {
                        AsyncImage(
                            model = imageData.imageUrl,
                            contentDescription = "uploaded image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        // 삭제 버튼
                        IconButton(
                            onClick = { onImageRemove(imageData.id) },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .size(24.dp)
                                .background(
                                    Color.Black.copy(alpha = 0.6f),
                                    CircleShape
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "삭제",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
                // 작은 추가 버튼
                item {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFD9D9D9))
                            .clickable {
                                if (!isUploading) {
                                    picker.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "추가",
                            tint = Color(0xFF646464),
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        }
        
        // 기존 이미지가 없을 때만 큰 추가 버튼 표시
        if (uploadedImages.isEmpty()) {
            Box(
                modifier = Modifier
                    .width(330.dp)
                    .aspectRatio(4f / 3f)
                    .padding(bottom = 30.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFD9D9D9))
                    .clickable {
                        if (!isUploading) {
                            picker.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                    }
            ) {
            if (hasImage) {
                Image(
                    painter = painter,
                    contentDescription = "picked",
                    modifier = Modifier
                        .width(330.dp)
                        .aspectRatio(4f / 3f)
                        .padding(bottom = 30.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop
                )
                
                // 업로드 중일 때 Progress 표시
                if (isUploading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "업로드 중...",
                                color = Color.White,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
                
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(4f / 3f)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF646464))
                            .clickable {
                                if (!isUploading) {
                                    picker.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "추가",
                            tint = Color.White,
                            modifier = Modifier.size(37.dp)
                        )
                    }
                }
            }
        }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryInput(
    categories: List<String>,
    selected: String?,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var query by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    val suggestions = remember(query, categories) {
        if (query.isBlank()) categories
        else categories.filter { it.contains(query, ignoreCase = true) }
    }

    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(Modifier.weight(1f))
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it },
            modifier = Modifier
                .alignByBaseline()
                .width(200.dp)
                .heightIn(35.dp)
        ) {
            TextField(
                value = selected ?: "",
                onValueChange = { },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .clickable { expanded = true },
                placeholder = { Text("", style = MaterialTheme.typography.bodyMedium) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null
                    )
                },
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium,
                readOnly = true,
                colors = colors(
                    focusedContainerColor = Color(0xFFD9D9D9),
                    unfocusedContainerColor = Color(0xFFD9D9D9),
                    focusedTextColor = Color(0xFF646464),
                    unfocusedTextColor = Color(0xFF646464),
                    focusedLeadingIconColor = Color(0xFF646464),
                    unfocusedLeadingIconColor = Color(0xFF646464),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent
                )
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { 
                    expanded = false
                }
            ) {
                suggestions.forEach { item ->
                    DropdownMenuItem(
                        text = { 
                            Text(
                                text = item,
                                style = MaterialTheme.typography.bodyMedium
                            ) 
                        },
                        onClick = {
                            onSelect(item)
                            query = ""
                            expanded = false
                        }
                    )
                }
                if (suggestions.isEmpty() && query.isNotBlank()) {
                    Divider()
                    DropdownMenuItem(
                        text = { 
                            Text(
                                text = "$query 새로 만들기",
                                style = MaterialTheme.typography.bodyMedium
                            ) 
                        },
                        onClick = {
                            onSelect(query)
                            query = ""
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun RowInfoInput(
    label: String,
    right: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 50.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Normal
            ),
            modifier = Modifier.alignByBaseline()
        )
        Spacer(Modifier.width(16.dp))
        Row(
            modifier = Modifier
                .alignByBaseline()
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            right()
        }
    }
}

fun uriToFile(context: Context, uri: Uri): File {
    val inputStream = context.contentResolver.openInputStream(uri)!!
    val tempFile = File.createTempFile("upload_", ".jpg", context.cacheDir)
    tempFile.outputStream().use { inputStream.copyTo(it) }
    return tempFile
}