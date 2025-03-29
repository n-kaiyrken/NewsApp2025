package kz.nkaiyrken.newsapp2025.presentation.newsdetails

sealed class NewsDetailsScreenState {

    data object Data : NewsDetailsScreenState()

    data object Initial : NewsDetailsScreenState()

    data object Loading : NewsDetailsScreenState()

}