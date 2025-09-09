package com.example.archiveandroid.feature.home.record
import android.graphics.BitmapFactory
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordScreen(
    onFilterClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0),
                title = {
                    Text(
                        text = "나의 활동 기록",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        ),
                        maxLines = 1
                    )
                },
                actions = {
                    IconButton(onClick = onFilterClick) {
                        Icon(
                            painter = rememberAssetIconPainter("icons/filter.png"),
                            contentDescription = "필터",
                            modifier = Modifier.size(28.dp),
                            tint = Color.Unspecified
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "기록", style = MaterialTheme.typography.titleLarge)
        }
    }
}

@Composable
private fun rememberAssetIconPainter(assetPath: String): Painter {
    val context = LocalContext.current
    val bitmap = remember(assetPath) {
        context.assets.open(assetPath).use { input ->
            BitmapFactory.decodeStream(input)
        }
    }
    return remember(bitmap) { BitmapPainter(bitmap.asImageBitmap()) }
}


