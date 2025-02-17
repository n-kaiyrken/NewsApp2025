package kz.nkaiyrken.newsapp2025.data.mapper

import kz.nkaiyrken.newsapp2025.data.model.ArticleDto
import kz.nkaiyrken.newsapp2025.domain.model.Article
import kz.nkaiyrken.newsapp2025.domain.model.DateInfo
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

class NewsMapper @Inject constructor() {

    fun mapNewsResposeToArticlesList(articlesDtoList: List<ArticleDto>): List<Article> {
        val articles = articlesDtoList.map { articleDto -> mapArticleDtoToArticle(articleDto) }
        return articles
    }

    fun mapArticleDtoToArticle(articleDto: ArticleDto): Article {
        return with(articleDto) {
            Article(
                sourceId = source.id,
                sourceName = source.name,
                title = title,
                description = description,
                author = author,
                url = url,
                imageUrl = urlToImage,
                publishedAt = mapTimestampToDate(parseDateToTimestamp(publishedAt)),
                content = content,
            )
        }
    }

    private fun mapTimestampToDate(timestamp: Long): DateInfo {
        val date = Date(timestamp * 1000)
        val calendar = Calendar.getInstance().apply { time = date }
        val today = Calendar.getInstance()
        val yesterday = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }

        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val fullDateFormat = SimpleDateFormat("d MMMM yyyy, HH:mm", Locale.getDefault())

        return when {
            calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                    calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR) -> {
                DateInfo.Today(timeFormat.format(date))
            }

            calendar.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR) &&
                    calendar.get(Calendar.DAY_OF_YEAR) == yesterday.get(Calendar.DAY_OF_YEAR) -> {
                DateInfo.Yesterday(timeFormat.format(date))
            }

            else -> DateInfo.FullDate(fullDateFormat.format(date))
        }
    }

    private fun parseDateToTimestamp(dateString: String): Long {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
        return formatter.parse(dateString)?.time?.div(1000) ?: 0L
    }
}