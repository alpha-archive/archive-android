package com.alpha.archiveandroid.core.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import com.alpha.archiveandroid.core.version.UpdateType

@Composable
fun UpdateDialog(
    updateType: UpdateType,
    playStoreUrl: String,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    
    val (title, message, confirmText, dismissible) = when (updateType) {
        UpdateType.REQUIRED -> {
            Tuple4(
                "필수 업데이트",
                "새로운 버전이 출시되었습니다.\n계속 사용하려면 업데이트가 필요합니다.",
                "업데이트",
                false
            )
        }
        UpdateType.OPTIONAL -> {
            Tuple4(
                "업데이트 안내",
                "새로운 버전이 출시되었습니다.\n더 나은 서비스를 위해 업데이트를 권장합니다.",
                "업데이트",
                true
            )
        }
        UpdateType.NONE -> return
    }
    
    AlertDialog(
        onDismissRequest = { if (dismissible) onDismiss() },
        title = { Text(text = title, fontWeight = FontWeight.Bold) },
        text = { Text(text = message) },
        confirmButton = {
            TextButton(
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(playStoreUrl))
                    context.startActivity(intent)
                    if (!dismissible) {
                        // 필수 업데이트는 Play Store로 이동 후 앱 종료
                        android.os.Process.killProcess(android.os.Process.myPid())
                    }
                }
            ) {
                Text(confirmText)
            }
        },
        dismissButton = if (dismissible) {
            {
                TextButton(onClick = onDismiss) {
                    Text("나중에")
                }
            }
        } else null
    )
}

private data class Tuple4<A, B, C, D>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D
)

