package kz.nkaiyrken.newsapp2025.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import kz.nkaiyrken.newsapp2025.presentation.newsdetails.NewsDetailsViewModel
import kz.nkaiyrken.newsapp2025.presentation.newslist.NewsListViewModel

@Module
interface ViewModelModule {

    @IntoMap
    @ViewModelKey(NewsListViewModel::class)
    @Binds
    fun bindNewsListViewModel(viewModel: NewsListViewModel): ViewModel

    @IntoMap
    @ViewModelKey(NewsDetailsViewModel::class)
    @Binds
    fun bindNewsDetailsViewModel(viewModel: NewsDetailsViewModel): ViewModel
}