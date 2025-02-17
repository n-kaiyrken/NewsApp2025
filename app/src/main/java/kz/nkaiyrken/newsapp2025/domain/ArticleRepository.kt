package kz.nkaiyrken.newsapp2025.domain

import kotlinx.coroutines.flow.StateFlow
import kz.nkaiyrken.newsapp2025.domain.model.Article
import kz.nkaiyrken.newsapp2025.domain.model.NewsResult

interface ArticleRepository {

    fun getArticlesByQuery(query: String): StateFlow<List<Article>>

    fun getTopHeadlines(): StateFlow<NewsResult?>

    fun setCategory(newCategory: String)

    suspend fun loadNextData()

    suspend fun loadNextTopHeadlines()

}