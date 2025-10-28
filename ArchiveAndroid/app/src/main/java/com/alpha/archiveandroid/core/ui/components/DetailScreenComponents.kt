package com.alpha.archiveandroid.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * 카테고리 표시용 버튼 컴포넌트
 */
@Composable
fun CategoryButton(
    text: String,
    backgroundColor: Color = Color(0xFFA0A6FF),
    textColor: Color = Color.White,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.SemiBold,
                color = textColor
            )
        )
    }
}

/**
 * 정보 행 표시 컴포넌트 (라벨-값 쌍)
 */
@Composable
fun DetailRowInfo(
    label: String,
    value: String? = null,
    modifier: Modifier = Modifier,
    valueContent: (@Composable () -> Unit)? = null // 커스텀 값 컨텐츠 (예: CategoryButton)
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 50.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Normal
            )
        )
        if (valueContent != null) {
            valueContent()
        } else {
            Text(
                text = value.orEmpty(),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Normal
                )
            )
        }
    }
}

/**
 * 메모 섹션 컴포넌트
 */
@Composable
fun DetailMemoSection(
    label: String = "메모",
    memo: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 50.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Normal
            )
        )
        Text(
            text = memo,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Normal
            ),
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

/**
 * 구분선 컴포넌트
 */
@Composable
fun DetailDivider(
    modifier: Modifier = Modifier
) {
    Divider(
        color = Color(0xffD9D9D9),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp, vertical = 10.dp)
    )
}

/**
 * 상세화면 정보 그룹 컴포넌트
 */
@Composable
fun DetailInfoGroup(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        content()
    }
}
