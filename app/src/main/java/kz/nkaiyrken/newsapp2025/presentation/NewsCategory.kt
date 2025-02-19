package kz.nkaiyrken.newsapp2025.presentation

import androidx.annotation.StringRes
import kz.nkaiyrken.newsapp2025.R

sealed class NewsCategory(val id: String, @StringRes val displayNameRes: Int) {
    object Sport : NewsCategory("sport", R.string.sport)
    object Business : NewsCategory("business", R.string.business)
    object Science : NewsCategory("science", R.string.science)
    object Health : NewsCategory("health", R.string.health)
    object Entertainment : NewsCategory("entertainment", R.string.entertainment)
    object Technology : NewsCategory("technology", R.string.technology)

    companion object {
        fun fromId(id: String): NewsCategory = when (id) {
            "sport" -> Sport
            "business" -> Business
            "science" -> Science
            "health" -> Health
            "entertainment" -> Entertainment
            "technology" -> Technology
            else -> Sport
        }
    }
}