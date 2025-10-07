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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults.colors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.archiveandroid.feature.home.record.input.data.remote.dto.RecordInputRequest
import com.example.archiveandroid.feature.home.record.input.view.ui.DateTimePicker
import com.example.archiveandroid.feature.home.record.input.view.ui.LocationPicker
import com.example.archiveandroid.feature.home.record.input.view.ui.Place
import com.example.yourapp.ui.components.TopAppBar
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
            RecordDraft(
                activityDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            )
        ) 
    }

    val categories = listOf("여행", "공부", "운동", "전시", "뮤지컬")

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
                                   // yyyy-MM-dd 형식을 yyyy-MM-ddT00:00:00.000Z 형식으로 변환
                                   "${dateStr}T00:00:00.000Z"
                               },
                               rating = draft.rating,
                               memo = draft.memo,
                               imageIds = ui.uploadedImages.map { it.id },
                            //    publicEventId = draft.publicEventId
                           )
                           onSave(req)
                    },
                    enabled = !ui.submitting,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("저장")
                }
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        contentWindowInsets = WindowInsets(0)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(top = 20.dp, bottom = 24.dp),
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
                }
            )

            RowInfoInput(label = "카테고리") {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    CategoryInput(
                        categories = categories,
                        selected = draft.category,
                        onSelect = {
//                            draft = draft.copy(category = it)
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
                Column {
                    LocationPicker(
                        selectedPlace = selectedPlace,
                        onPlaceSelected = { place ->
                            selectedPlace = place
                            draft = draft.copy(location = place.name)
                        }
                    )
                    if (selectedPlace == null) {
                        Text("사진 업로드 시 자동 등록",
                            color = Color(0xFF898989),
                            fontWeight = FontWeight.Light,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
            Divider(color = Color(0xffD9D9D9), modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp, vertical = 10.dp))

            RowInfoInput(label = "메모") {
                TextField(
                    value = draft.memo,
                    onValueChange = { draft = draft.copy(memo = it) },
                    singleLine = true,
                    colors = textfieldColors,
                    modifier = Modifier
                        .widthIn(min = 180.dp, max = 420.dp)
                        .heightIn(min = 44.dp)
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
    val category: String = "MUSICAL",
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
    onPick: (Uri?) -> Unit
) {
    val picker = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri -> onPick(uri) }

    val painter = rememberAsyncImagePainter(model = imageUri)
    val hasImage = imageUri != null

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 50.dp)
    ) {
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
                                fontSize = 14.sp
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
                .width(100.dp)
                .heightIn(35.dp)
        ) {
            TextField(
                value = query,
                onValueChange = { query = it; expanded = true },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                placeholder = { Text("검색",  fontSize = 15.sp) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null
                    )
                },
                singleLine = true,
                textStyle = TextStyle( fontSize = 15.sp),
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
                expanded = expanded && (suggestions.isNotEmpty() || query.isNotBlank()),
                onDismissRequest = { expanded = false }
            ) {
                suggestions.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(item) },
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
                        text = { Text("“$query” 새로 만들기") },
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
            style = TextStyle( fontWeight = FontWeight.Normal, fontSize = 18.sp),
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