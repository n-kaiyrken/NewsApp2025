package kz.nkaiyrken.newsapp2025.domain.usecase

import kz.nkaiyrken.newsapp2025.domain.ArticleRepository
import javax.inject.Inject

class SetCategoryUseCase @Inject constructor(
    private val repository: ArticleRepository,
) {

    operator fun invoke(newCategory: String) {
        repository.setCategory(newCategory)
    }
}