package kz.nkaiyrken.newsapp2025.presentation.newslist

import kz.nkaiyrken.newsapp2025.domain.model.Article

sealed class NewsListScreenState {

    data class Content(
        val articles: List<Article>,
        val nextDataIsLoading: Boolean = false,
        val isLastPage: Boolean = false,
        val selectedCategory: String = "",
    ) : NewsListScreenState()

    object Initial : NewsListScreenState()

    object Loading : NewsListScreenState()

}