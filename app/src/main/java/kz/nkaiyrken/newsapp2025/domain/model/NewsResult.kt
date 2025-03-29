package kz.nkaiyrken.newsapp2025.domain.model

data class NewsResult(
    val articles: List<Article> = emptyList(),
    val isLastPage: Boolean = false,
    val isLoadingFailed: Boolean = false,
    val isInitialResult: Boolean = false,
)
