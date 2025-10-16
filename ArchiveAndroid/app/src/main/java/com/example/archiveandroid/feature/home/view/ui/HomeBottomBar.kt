package com.example.archiveandroid.feature.home.ui

import android.graphics.BitmapFactory
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun HomeBottomBar(
    navController: NavHostController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
        homeDestinations.forEach { dest ->
            val selected = currentRoute == dest.route.route
            NavigationBarItem(
                selected = selected,
                onClick = { navController.navigateSingleTopTo(dest.route.route) },
                icon = {
                    val size = if (selected) 30.dp else 24.dp
                    Icon(
                        painter = rememberAssetIconPainter(dest.assetIconPath),
                        contentDescription = dest.label,
                        modifier = Modifier.size(size),
                        tint = Color.Unspecified
                    )
                },
                label = { Text(dest.label) },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.surface
                )
            )
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



