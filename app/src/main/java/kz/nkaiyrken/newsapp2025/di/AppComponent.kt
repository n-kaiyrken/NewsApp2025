package kz.nkaiyrken.newsapp2025.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import kz.nkaiyrken.newsapp2025.presentation.ViewModelFactory
import kz.nkaiyrken.newsapp2025.presentation.profile.ProfileViewModel

@ApplicationScope
@Component(modules = [AppModule::class, ViewModelModule::class])
interface AppComponent {

    fun getViewModelFactory(): ViewModelFactory
    fun getProfileViewModel(): ProfileViewModel

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance application: Context,
        ): AppComponent
    }
}