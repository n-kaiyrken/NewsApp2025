package kz.nkaiyrken.newsapp2025.presentation.newsdetails

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class NewsDetailsViewModel @Inject constructor() : ViewModel() {

    val screenState = MutableStateFlow<NewsDetailsScreenState>(NewsDetailsScreenState.Content)

}