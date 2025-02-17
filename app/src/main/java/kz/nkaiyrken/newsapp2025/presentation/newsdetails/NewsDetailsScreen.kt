package kz.nkaiyrken.newsapp2025.presentation.newsdetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import kz.nkaiyrken.newsapp2025.R
import kz.nkaiyrken.newsapp2025.domain.model.Article
import kz.nkaiyrken.newsapp2025.domain.model.DateInfo
import kz.nkaiyrken.newsapp2025.getApplicationComponent
import kz.nkaiyrken.newsapp2025.ui.utils.formatDateString

@Composable
fun NewsDetailsScreen(
    article: Article,
    onBackClick: () -> Unit,
    paddings: PaddingValues,
) {
    val component = getApplicationComponent()
    val viewModel: NewsDetailsViewModel = viewModel(factory = component.getViewModelFactory())
    val screenState = viewModel.screenState.collectAsState(NewsDetailsScreenState.Initial)

    Box(
        modifier = Modifier.padding(paddings)
    ) {
        when (val currentState = screenState.value) {
            is NewsDetailsScreenState.Initial -> { /* Пустой экран или загрузка */
            }
            is NewsDetailsScreenState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }
            is NewsDetailsScreenState.Content -> {
                NewsDetailsScreenContent(article = article)
            }
        }
    }
}

@Composable
fun NewsDetailsScreenContent(
    article: Article,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        item {
            article.imageUrl?.let { imageUrl ->
                AsyncImage(
                    model = imageUrl,
                    contentDescription = stringResource(R.string.article_image),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = android.R.drawable.ic_menu_report_image),
                    error = painterResource(id = android.R.drawable.ic_menu_report_image)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            Text(
                text = article.title,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = formatDateString(LocalContext.current, article.publishedAt),
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )

                Spacer(modifier = Modifier.weight(1f))

                article.sourceName?.takeIf { it.isNotBlank() }?.let { source ->
                    ClickableText(
                        text = AnnotatedString(source),
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.primary,
                            textDecoration = TextDecoration.Underline
                        ),
                        onClick = {
                            // Здесь можно добавить логику для открытия ссылки,
                            // например, если article.url содержит ссылку на источник.
                        },
                        modifier = Modifier.widthIn(max = 150.dp),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            article.author?.takeIf { it.isNotBlank() }?.let { author ->
                Text(
                    text = "By $author",
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            Text(
                text = article.content.orEmpty(),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NewsDetailsScreenContentPreview() {
    val sampleArticle = Article(
        sourceId = "1",
        sourceName = "Sample News Source",
        author = "John Doe",
        title = "Sample News Title: Kotlin and Jetpack Compose",
        description = "This is a sample news description for preview purposes.",
        url = "https://example.com",
        imageUrl = "https://via.placeholder.com/800x400",
        publishedAt = DateInfo.FullDate("2025-02-17T12:00:00Z"),  // Предполагаемая структура DateInfo
        content = "Here is the main content of the sample article. It explains how to use Jetpack Compose to create reactive UIs in a modern Android application."
    )

    MaterialTheme {
        NewsDetailsScreenContent(article = sampleArticle)
    }
}
