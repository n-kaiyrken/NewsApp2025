package kz.nkaiyrken.newsapp2025.domain.model

data class Article(
    val sourceId: String?,
    val sourceName: String?,
    val author: String?,
    val title: String,
    val description: String?,
    val url: String,
    val imageUrl: String?,
    val publishedAt: DateInfo,
    val content: String?,
)
