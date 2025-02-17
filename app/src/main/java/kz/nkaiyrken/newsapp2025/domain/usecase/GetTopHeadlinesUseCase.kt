package kz.nkaiyrken.newsapp2025.domain.usecase

import kotlinx.coroutines.flow.StateFlow
import kz.nkaiyrken.newsapp2025.domain.ArticleRepository
import kz.nkaiyrken.newsapp2025.domain.model.NewsResult
import javax.inject.Inject

class GetTopHeadlinesUseCase @Inject constructor(
    private val repository: ArticleRepository,
) {

    operator fun invoke(): StateFlow<NewsResult?> {
        return repository.getTopHeadlines()
    }

}