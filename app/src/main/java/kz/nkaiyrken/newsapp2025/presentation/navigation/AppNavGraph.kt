package kz.nkaiyrken.newsapp2025.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import kz.nkaiyrken.newsapp2025.domain.model.Article
import kz.nkaiyrken.newsapp2025.getApplicationComponent
import kz.nkaiyrken.newsapp2025.presentation.profile.ProfileScreen
import kz.nkaiyrken.newsapp2025.ui.utils.JsonUtils

@Composable
fun AppNavGraph(
    navigationState: NavigationState,
    newsListScreenContent: @Composable () -> Unit,
    detailsScreenContent: @Composable (Article) -> Unit,
) {

    NavHost(
        startDestination = Screens.NewsList.route,
        navController = navigationState.navHostController
    ) {
        composable(route = Screens.NewsList.route) {
            newsListScreenContent()
        }
        composable(
            route = Screens.Details.route,
            arguments = listOf(
                navArgument(Screens.KEY_ARTICLE) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val articleJson = backStackEntry.arguments?.getString(Screens.KEY_ARTICLE) ?: ""
            val article = JsonUtils.gson.fromJson(articleJson, Article::class.java)
            detailsScreenContent(article)
        }
        composable(route = Screens.Profile.route) {
            val component = getApplicationComponent()
            ProfileScreen(
                viewModel = component.getProfileViewModel(),
                onNavigateBack = { navigationState.navHostController.popBackStack() }
            )
        }
    }
}