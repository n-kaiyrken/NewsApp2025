package kz.nkaiyrken.newsapp2025.presentation.newsdetails

import kz.nkaiyrken.newsapp2025.domain.model.Article

sealed class NewsDetailsScreenState {

    data object Content : NewsDetailsScreenState()

    data object Initial : NewsDetailsScreenState()

    data object Loading : NewsDetailsScreenState()

}