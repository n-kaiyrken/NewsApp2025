package kz.nkaiyrken.newsapp2025.di

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import kz.nkaiyrken.newsapp2025.data.network.ApiFactory
import kz.nkaiyrken.newsapp2025.data.network.ApiService
import kz.nkaiyrken.newsapp2025.data.repository.ArticleRepositoryImpl
import kz.nkaiyrken.newsapp2025.domain.ArticleRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
interface AppModule {

    @ApplicationScope
    @Binds
    fun bindRepository(articleRepositoryImpl: ArticleRepositoryImpl): ArticleRepository

    companion object {
        @Provides
        @ApplicationScope
        fun provideContext(application: Application): Context = application.applicationContext

        @Provides
        @ApplicationScope
        fun provideApiService(): ApiService {
            return ApiFactory.apiService
        }

        // @Singleton
//    fun provideDatabase(context: Context): NewsDatabase {
//        return Room.databaseBuilder(context, NewsDatabase::class.java, "news_db").build()
//    }
//
//    @Provides
//    fun provideDao(db: NewsDatabase) = db.newsDao()
    }

}