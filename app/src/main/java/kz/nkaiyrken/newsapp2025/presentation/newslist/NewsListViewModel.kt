package kz.nkaiyrken.newsapp2025.presentation.newslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kz.nkaiyrken.newsapp2025.domain.model.NewsResult
import kz.nkaiyrken.newsapp2025.domain.usecase.GetTopHeadlinesUseCase
import kz.nkaiyrken.newsapp2025.domain.usecase.LoadNextHeadlinesUseCase
import kz.nkaiyrken.newsapp2025.domain.usecase.SetCategoryUseCase
import kz.nkaiyrken.newsapp2025.extensions.tryToUpdate
import kz.nkaiyrken.newsapp2025.presentation.NewsCategory
import javax.inject.Inject

class NewsListViewModel @Inject constructor(
    getTopHeadlinesUseCase: GetTopHeadlinesUseCase,
    private val loadNextHeadlinesUseCase: LoadNextHeadlinesUseCase,
    private val setCategoryUseCase: SetCategoryUseCase,
) : ViewModel() {

    private val topHeadlinesFlow = getTopHeadlinesUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = NewsResult(isInitialResult = true)
        )

    private val uiEventFlow = MutableStateFlow(NewsListScreenState.Data(emptyList()))

    val screenState = combine(
        topHeadlinesFlow,
        uiEventFlow
    ) { newsResult, uiState ->
        when {
            newsResult.isInitialResult -> NewsListScreenState.Loading
            newsResult.isLoadingFailed -> uiState.copy(isLoadingFailed = true)
            else -> uiState.copy(
                articles = newsResult.articles,
                nextDataIsLoading = false,
                isLastPage = newsResult.isLastPage,
            )
        }
    }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            NewsListScreenState.Loading
        )

    fun loadNextNews() {
        viewModelScope.launch {
            val currentResult = topHeadlinesFlow.value
            if (currentResult.isLastPage || currentResult.isLoadingFailed) return@launch
            uiEventFlow.tryToUpdate {
                it.copy(nextDataIsLoading = true)
            }
            loadNextHeadlinesUseCase()
        }
    }

    fun setCategory(newCategory: String) {
        uiEventFlow.tryToUpdate {
            it.copy(
                selectedCategory = NewsCategory.fromId(newCategory),
                articles = emptyList()
            )
        }
        setCategoryUseCase(newCategory)
    }
}