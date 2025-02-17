package kz.nkaiyrken.newsapp2025.data.model

data class ArticleDto(
    val source: SourceDto,
    val author: String,
    val title: String,
    val description: String?,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String,
    val content: String?,
)
