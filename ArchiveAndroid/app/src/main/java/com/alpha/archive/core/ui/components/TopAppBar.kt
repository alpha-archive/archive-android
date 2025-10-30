package com.alpha.archive.core.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*

@Immutable
data class AppBarMenuItem(
    val id: String,
    val label: String,
    val onClick: () -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    title: String,
    showBack: Boolean = true,
    onBackClick: (() -> Unit)? = null,
    menuItems: List<AppBarMenuItem> = emptyList(),
    scrollBehavior: TopAppBarScrollBehavior? = null,
    actions: @Composable RowScope.() -> Unit = {}
) {
    var menuOpen by remember { mutableStateOf(false) }

    TopAppBar(
        title = { 
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            ) 
        },
        navigationIcon = {
            if (showBack) {
                IconButton(onClick = { onBackClick?.invoke() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "뒤로가기"
                    )
                }
            }
        },
        actions = {
            actions()

            if (menuItems.isNotEmpty()) {
                IconButton(onClick = { menuOpen = true }) {
                    Icon(Icons.Filled.MoreVert, contentDescription = "더보기")
                }
                DropdownMenu(expanded = menuOpen, onDismissRequest = { menuOpen = false }) {
                    menuItems.forEach { item ->
                        DropdownMenuItem(
                            text = { 
                                Text(
                                    text = item.label,
                                    style = MaterialTheme.typography.bodyMedium
                                ) 
                            },
                            onClick = {
                                menuOpen = false
                                item.onClick()
                            }
                        )
                    }
                }
            }
        },
        scrollBehavior = scrollBehavior
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CenteredAppTopBar(
    title: String,
    showBack: Boolean = true,
    onBackClick: (() -> Unit)? = null,
    menuItems: List<AppBarMenuItem> = emptyList(),
    scrollBehavior: TopAppBarScrollBehavior? = null,
    actions: @Composable RowScope.() -> Unit = {}
) {
    var menuOpen by remember { mutableStateOf(false) }

    CenterAlignedTopAppBar(
        title = { 
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            ) 
        },
        navigationIcon = {
            if (showBack) {
                IconButton(onClick = { onBackClick?.invoke() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "뒤로가기"
                    )
                }
            }
        },
        actions = {
            actions()
            if (menuItems.isNotEmpty()) {
                IconButton(onClick = { menuOpen = true }) {
                    Icon(Icons.Filled.MoreVert, contentDescription = "더보기")
                }
                DropdownMenu(expanded = menuOpen, onDismissRequest = { menuOpen = false }) {
                    menuItems.forEach { item ->
                        DropdownMenuItem(
                            text = { 
                                Text(
                                    text = item.label,
                                    style = MaterialTheme.typography.bodyMedium
                                ) 
                            },
                            onClick = {
                                menuOpen = false
                                item.onClick()
                            }
                        )
                    }
                }
            }
        },
        scrollBehavior = scrollBehavior
    )
}
