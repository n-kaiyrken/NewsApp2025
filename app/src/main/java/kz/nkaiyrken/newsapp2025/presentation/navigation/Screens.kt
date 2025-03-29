package kz.nkaiyrken.newsapp2025.presentation.navigation

import android.net.Uri
import kz.nkaiyrken.newsapp2025.domain.model.Article
import kz.nkaiyrken.newsapp2025.ui.utils.JsonUtils

sealed class Screens(
    val route: String,
) {

    data object NewsList : Screens(ROUTE_NEWS)
    data object Details : Screens(ROUTE_DETAILS) {
        const val ROUTE_FOR_ARGS = "details"

        fun getRouteWithArgs(article: Article): String {
            val articleJson = JsonUtils.gson.toJson(article)
            return "$ROUTE_FOR_ARGS/${articleJson.encode()}"
        }
    }
    data object Profile : Screens(ROUTE_PROFILE)

    companion object {
        const val KEY_ARTICLE = "article"

        const val ROUTE_NEWS = "news_list"
        const val ROUTE_DETAILS = "details/{$KEY_ARTICLE}"
        const val ROUTE_PROFILE = "profile"
    }
}

fun String.encode(): String {
    return Uri.encode(this)
}