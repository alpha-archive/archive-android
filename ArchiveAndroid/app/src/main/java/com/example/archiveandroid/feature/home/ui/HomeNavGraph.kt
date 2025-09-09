package com.example.archiveandroid.feature.home.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

enum class HomeRoute(val route: String) {
    Recommend("recommend"),
    Record("record"),
    Chatbot("chatbot"),
    Stats("stats"),
    User("user")
}

data class HomeDestination(
    val route: HomeRoute,
    val label: String,
    val icon: ImageVector
)

val homeDestinations = listOf(
    HomeDestination(HomeRoute.Recommend, "추천", Icons.Filled.Home),
    HomeDestination(HomeRoute.Record, "기록", Icons.Filled.History),
    HomeDestination(HomeRoute.Chatbot, "챗봇", Icons.Filled.Chat),
    HomeDestination(HomeRoute.Stats, "통계", Icons.Filled.AutoGraph),
    HomeDestination(HomeRoute.User, "사용자", Icons.Filled.Person)
)

fun NavHostController.navigateSingleTopTo(route: String) {
    this.navigate(route) {
        popUpTo(this@navigateSingleTopTo.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

@Composable
fun HomeNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = HomeRoute.Recommend.route,
        modifier = modifier
    ) {
        composable(HomeRoute.Recommend.route) { RecommendScreen() }
        composable(HomeRoute.Record.route) { RecordScreen() }
        composable(HomeRoute.Chatbot.route) { ChatbotScreen() }
        composable(HomeRoute.Stats.route) { StatsScreen() }
        composable(HomeRoute.User.route) { UserScreen() }
    }
}


