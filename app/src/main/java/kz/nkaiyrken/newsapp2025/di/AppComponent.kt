package kz.nkaiyrken.newsapp2025.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import kz.nkaiyrken.newsapp2025.presentation.ViewModelFactory

@ApplicationScope
@Component(modules = [AppModule::class, ViewModelModule::class])
interface AppComponent {

    fun getViewModelFactory(): ViewModelFactory

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance application: Context,
        ): AppComponent
    }
}