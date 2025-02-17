package kz.nkaiyrken.newsapp2025.domain.usecase

import kotlinx.coroutines.flow.StateFlow
import kz.nkaiyrken.newsapp2025.domain.ArticleRepository
import kz.nkaiyrken.newsapp2025.domain.model.Article
import javax.inject.Inject

class GetArticlesByQueryUseCase @Inject constructor(
    val repository: ArticleRepository,
) {

    operator fun invoke(query: String): StateFlow<List<Article>> {
        return repository.getArticlesByQuery(query)
    }

}
