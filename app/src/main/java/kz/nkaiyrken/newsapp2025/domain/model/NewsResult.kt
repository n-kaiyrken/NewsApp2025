package kz.nkaiyrken.newsapp2025.domain.model

data class NewsResult(
    val articles: List<Article>,
    val isLastPage: Boolean = false,
)
