package kz.nkaiyrken.newsapp2025.domain.usecase

import kz.nkaiyrken.newsapp2025.domain.ArticleRepository
import javax.inject.Inject

class LoadNextHeadlinesUseCase @Inject constructor(
    private val repository: ArticleRepository,
) {

    suspend operator fun invoke() {
        repository.loadNextTopHeadlines()
    }
}
