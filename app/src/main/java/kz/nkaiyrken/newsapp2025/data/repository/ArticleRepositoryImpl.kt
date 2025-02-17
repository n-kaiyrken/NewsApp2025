package kz.nkaiyrken.newsapp2025.data.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.stateIn
import kz.nkaiyrken.newsapp2025.data.mapper.NewsMapper
import kz.nkaiyrken.newsapp2025.data.network.ApiService
import kz.nkaiyrken.newsapp2025.domain.ArticleRepository
import kz.nkaiyrken.newsapp2025.domain.model.Article
import kz.nkaiyrken.newsapp2025.domain.model.NewsResult
import javax.inject.Inject

class ArticleRepositoryImpl @Inject constructor(
    private val mapper: NewsMapper,
    private val apiService: ApiService,
) : ArticleRepository {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private val _articles =
        mutableListOf<Article>() //это не нужно, если нет функционала удаление статьи
    private val articles: List<Article>
        get() = _articles.toList()

    private val _topHeadlinesList =
        mutableListOf<Article>() //это не нужно, если нет функционала удаление статьи
    private val topHeadlinesList: List<Article>
        get() = _topHeadlinesList.toList()

    private val selectedCategory = MutableStateFlow(DEFAULT_CATEGORY)

    private var currentArticlesPage: Int = 0
    private val currentQuery: String? = null
    private var totalResults: Int = 1

    private var currentHeadlinesPage: Int = 1
    private var totalHeadlinesPages: Int = 0

    private val nextDataNeededEvents = MutableSharedFlow<Unit>(replay = 1)
    private val nextTopHeadlinesNeededEvents = MutableSharedFlow<Unit>(replay = 1)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val loadTopHeadlines = selectedCategory.flatMapLatest { category ->
        flow {
            nextTopHeadlinesNeededEvents.emit(Unit)
            nextTopHeadlinesNeededEvents.collect {
                if (currentHeadlinesPage > totalHeadlinesPages && totalHeadlinesPages != 0) {
                    emit(NewsResult(articles = topHeadlinesList, isLastPage = true))
                    return@collect
                }
                val response =
                    apiService.getTopHeadlines(
                        apiKey = API_KEY,
                        page = currentHeadlinesPage,
                        pageSize = PAGE_SIZE,
                        category = category
                    )
                if (response.articles.isNullOrEmpty()) {
                    emit(NewsResult(articles = emptyList()))
                    return@collect
                }
                currentHeadlinesPage += 1
                totalHeadlinesPages = (response.totalResults + PAGE_SIZE - 1) / PAGE_SIZE
                val mappedArticles = mapper.mapNewsResposeToArticlesList(response.articles)
                _topHeadlinesList.addAll(mappedArticles)
                emit(NewsResult(articles = topHeadlinesList))
            }
        }
    }.retry {
        delay(RETRY_TIMEOUT_MILLIS)
        true
    }.stateIn(
        scope = coroutineScope,
        initialValue = null,
        started = SharingStarted.Lazily
    )

    override fun getTopHeadlines(): StateFlow<NewsResult?> = loadTopHeadlines

    override suspend fun loadNextTopHeadlines() {
        nextTopHeadlinesNeededEvents.emit(Unit)
    }

    override fun setCategory(newCategory: String) {
        selectedCategory.value = newCategory
        currentHeadlinesPage = 1
        _topHeadlinesList.clear()
    }

    override fun getArticlesByQuery(query: String): StateFlow<List<Article>> = flow {
        nextDataNeededEvents.emit(Unit)
        nextDataNeededEvents.collect {
            val totalPages = (totalResults + PAGE_SIZE - 1) / PAGE_SIZE
            if (currentArticlesPage >= totalPages) return@collect
            val response = apiService.getArticles(
                apiKey = API_KEY,
                page = currentArticlesPage + 1,
                query = query
            )
            currentArticlesPage += 1
            emit(articles)
        }
    }.retry {
        delay(1000)
        true
    }.stateIn(
        scope = coroutineScope,
        initialValue = emptyList(),
        started = SharingStarted.Lazily
    )

    override suspend fun loadNextData() {
        nextDataNeededEvents.emit(Unit)
    }

    companion object {
        private const val DEFAULT_CATEGORY = "sport"
        private const val API_KEY = "b97a8a9834434ab6a3e19f83057388f3"
        private const val RETRY_TIMEOUT_MILLIS = 3000L
        private const val PAGE_SIZE = 12
    }


}