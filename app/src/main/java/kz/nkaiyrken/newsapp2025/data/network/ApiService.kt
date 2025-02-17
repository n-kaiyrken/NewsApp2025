package kz.nkaiyrken.newsapp2025.data.network

import kz.nkaiyrken.newsapp2025.data.model.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("everything")
    suspend fun getArticles(
        @Query("q") query: String,
        @Query("apiKey") apiKey: String,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int = 20,
    ): NewsResponse

    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("category") category: String = "sport",
        @Query("apiKey") apiKey: String,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int,
    ): NewsResponse
}