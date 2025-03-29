package kz.nkaiyrken.newsapp2025.domain.model

data class UserProfile(
    val id: String,
    val name: String,
    val email: String,
    val avatarUrl: String? = null
) 