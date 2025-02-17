package kz.nkaiyrken.newsapp2025.presentation.newslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kz.nkaiyrken.newsapp2025.domain.usecase.GetTopHeadlinesUseCase
import kz.nkaiyrken.newsapp2025.domain.usecase.LoadNextHeadlinesUseCase
import kz.nkaiyrken.newsapp2025.domain.usecase.SetCategoryUseCase
import kz.nkaiyrken.newsapp2025.extensions.mergeWith
import javax.inject.Inject

class NewsListViewModel @Inject constructor(
    val getTopHeadlinesUseCase: GetTopHeadlinesUseCase,
    val loadNextHeadlinesUseCase: LoadNextHeadlinesUseCase,
    val setCategoryUseCase: SetCategoryUseCase,
) : ViewModel() {

    private val topHeadlinesFlow = getTopHeadlinesUseCase()

    private val uiEventFlow = MutableSharedFlow<NewsListScreenState.Content>()

    val screenState = topHeadlinesFlow
        .map { newsResult ->
            if (newsResult == null) NewsListScreenState.Loading
            else NewsListScreenState.Content(articles = newsResult.articles)
        }
        .onStart { emit(NewsListScreenState.Loading) }
        .mergeWith(uiEventFlow)

    fun loadNextNews() {
        viewModelScope.launch {
            val currentResult = topHeadlinesFlow.value ?: return@launch
            if (currentResult.isLastPage) {
                uiEventFlow.emit(
                    NewsListScreenState.Content(
                        articles = currentResult.articles,
                        isLastPage = true
                    )
                )
                return@launch
            } else {
                uiEventFlow.emit(
                    NewsListScreenState.Content(
                        articles = currentResult.articles,
                        nextDataIsLoading = true
                    )
                )
                loadNextHeadlinesUseCase()
            }
        }

        fun setCategory(newCategory: String) {

            setCategoryUseCase(newCategory)
        }
//        private suspend fun updateScreenState(update: (NewsListScreenState.Content) -> NewsListScreenState) {
//            uiEventFlow.emit { currentState ->
//                if (currentState is NewsListScreenState.Content) update(currentState) else currentState
//            }
//        }

//        private fun NewsListScreenState.Content.copyOrSame(
//            isLastPage: Boolean? = null,
//            nextDataIsLoading: Boolean? = null,
//        ): NewsListScreenState.Content {
//            return this.copy(
//                isLastPage = isLastPage ?: this.isLastPage,
//                nextDataIsLoading = nextDataIsLoading ?: this.nextDataIsLoading
//            )
//        }

    }
}