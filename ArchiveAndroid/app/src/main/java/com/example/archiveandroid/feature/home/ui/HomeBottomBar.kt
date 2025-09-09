package com.example.archiveandroid.feature.home.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.NavHostController

@Composable
fun HomeBottomBar(
    navController: NavHostController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        homeDestinations.forEach { dest ->
            val selected = currentRoute == dest.route.route
            NavigationBarItem(
                selected = selected,
                onClick = { navController.navigateSingleTopTo(dest.route.route) },
                icon = { Icon(dest.icon, contentDescription = dest.label) },
                label = { Text(dest.label) }
            )
        }
    }
}


