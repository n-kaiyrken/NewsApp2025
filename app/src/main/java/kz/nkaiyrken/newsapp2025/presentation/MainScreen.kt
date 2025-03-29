package kz.nkaiyrken.newsapp2025.presentation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import kz.nkaiyrken.newsapp2025.presentation.navigation.AppNavGraph
import kz.nkaiyrken.newsapp2025.presentation.navigation.Screens
import kz.nkaiyrken.newsapp2025.presentation.navigation.rememberNavigationState
import kz.nkaiyrken.newsapp2025.presentation.newsdetails.NewsDetailsScreen
import kz.nkaiyrken.newsapp2025.presentation.newslist.NewsListScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navigationState = rememberNavigationState()
    val currentBackStackEntry by navigationState.navHostController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val canNavigateBack = currentRoute != Screens.NewsList.route
    val showProfileIcon = currentRoute != Screens.Profile.route

    Scaffold(
        topBar = {
            CustomTopAppBar(
                canNavigateBack = canNavigateBack,
                onNavigationIconClick = {
                    if (canNavigateBack) {
                        navigationState.navHostController.popBackStack()
                    } else {
                        // Открываем меню или другие действия
                    }
                },
                onLanguageSelected = {},
                onProfileClick = {
                    navigationState.navigateTo(Screens.Profile.route)
                },
                showProfileIcon = showProfileIcon
            )
        }
    ) { innerPadding ->
        AppNavGraph(
            navigationState = navigationState,
            newsListScreenContent = {
                NewsListScreen(
                    paddings = innerPadding,
                    onArticleClick = { article ->
                        navigationState.navigateTo(Screens.Details.getRouteWithArgs(article))
                    }
                )
            },
            detailsScreenContent = {
                NewsDetailsScreen(
                    article = it,
                    onBackClick = { navigationState.navHostController.popBackStack() },
                    paddings = innerPadding
                )
            },
        )
    }
}