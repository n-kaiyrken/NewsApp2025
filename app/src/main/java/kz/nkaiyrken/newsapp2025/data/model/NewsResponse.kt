package kz.nkaiyrken.newsapp2025.data.model

data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<ArticleDto>?
)
