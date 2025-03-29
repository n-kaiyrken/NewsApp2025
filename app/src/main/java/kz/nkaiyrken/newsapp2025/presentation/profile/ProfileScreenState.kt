package kz.nkaiyrken.newsapp2025.presentation.profile

import kz.nkaiyrken.newsapp2025.domain.model.UserProfile

sealed class ProfileScreenState {
    object Initial : ProfileScreenState()
    object Loading : ProfileScreenState()
    data class Success(
        val profile: UserProfile,
        val stats: ProfileStats = ProfileStats()
    ) : ProfileScreenState()
    data class Error(val message: String) : ProfileScreenState()
}

data class ProfileStats(
    val ordersCount: Int = 0,
    val favoritesCount: Int = 0
) 