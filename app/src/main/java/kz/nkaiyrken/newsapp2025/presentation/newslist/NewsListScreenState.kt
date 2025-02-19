package kz.nkaiyrken.newsapp2025.presentation.newslist

import kz.nkaiyrken.newsapp2025.domain.model.Article
import kz.nkaiyrken.newsapp2025.presentation.NewsCategory

sealed class NewsListScreenState {

    data class Data(
        val articles: List<Article>,
        val nextDataIsLoading: Boolean = false,
        val isLastPage: Boolean = false,
        val isLoadingFailed: Boolean = false,
        val selectedCategory: NewsCategory = NewsCategory.Sport,
    ) : NewsListScreenState()

    object Initial : NewsListScreenState()

    object Loading : NewsListScreenState()

}