package kz.nkaiyrken.newsapp2025.data.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
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

    private val repositoryScope = CoroutineScope(Dispatchers.IO)

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

    init {
        // Инициируем загрузку первой страницы
        repositoryScope.launch {
            nextTopHeadlinesNeededEvents.emit(Unit)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val loadTopHeadlines = selectedCategory.flatMapLatest { category ->
        flow {
            //nextTopHeadlinesNeededEvents.emit(Unit)
            emit(NewsResult(isInitialResult = true))
            nextTopHeadlinesNeededEvents.first()

            while (true) {
                // Ждём следующее событие загрузки
                if (currentHeadlinesPage > totalHeadlinesPages && totalHeadlinesPages != 0) {
                    emit(NewsResult(articles = topHeadlinesList, isLastPage = true))
                    break
                }
                val response =
                    apiService.getTopHeadlines(
                        apiKey = API_KEY,
                        page = currentHeadlinesPage,
                        pageSize = PAGE_SIZE,
                        category = category
                    )
                if (response.articles.isNullOrEmpty()) {
                    emit(NewsResult(isLoadingFailed = true))
                    break
                }
                currentHeadlinesPage += 1
                totalHeadlinesPages = (response.totalResults + PAGE_SIZE - 1) / PAGE_SIZE
                val mappedArticles = mapper.mapNewsResposeToArticlesList(response.articles)
                _topHeadlinesList.addAll(mappedArticles)
                emit(
                    NewsResult(
                        articles = topHeadlinesList,
                        isLastPage = currentHeadlinesPage > totalHeadlinesPages
                    )
                )
                nextTopHeadlinesNeededEvents.first()
            }
        }
    }.retryWhen { cause, attempt ->
        val maxRetries = 3
        if (attempt < maxRetries) {
            delay(RETRY_TIMEOUT_MILLIS)
            true
        } else {
            false
        }
    }
        .catch { e ->
            emit(NewsResult(isLoadingFailed = true))
        }
        .flowOn(Dispatchers.IO)


    override fun getTopHeadlines(): Flow<NewsResult> = loadTopHeadlines

    override suspend fun loadNextTopHeadlines() {
        nextTopHeadlinesNeededEvents.emit(Unit)
    }

    override fun setCategory(newCategory: String) {
        // Обновляем выбранную категорию и сбрасываем состояние пагинации
        selectedCategory.value = newCategory
        currentHeadlinesPage = 1
        totalHeadlinesPages = 0
        _topHeadlinesList.clear()
        // Сразу запускаем загрузку для новой категории
        repositoryScope.launch {
            nextTopHeadlinesNeededEvents.emit(Unit)
        }
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
        scope = repositoryScope,
        initialValue = emptyList(),
        started = SharingStarted.Lazily
    )

    override suspend fun loadNextData() {
        nextDataNeededEvents.emit(Unit)
    }

    companion object {
        private const val DEFAULT_CATEGORY = "sport"
        private const val API_KEY = "9c3698288a09433187abf9f7b0af08a7"
        private const val RETRY_TIMEOUT_MILLIS = 3000L
        private const val PAGE_SIZE = 12
    }


}