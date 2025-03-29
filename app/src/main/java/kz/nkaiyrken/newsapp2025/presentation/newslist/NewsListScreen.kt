package kz.nkaiyrken.newsapp2025.presentation.newslist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kz.nkaiyrken.newsapp2025.R
import kz.nkaiyrken.newsapp2025.domain.model.Article
import kz.nkaiyrken.newsapp2025.domain.model.DateInfo
import kz.nkaiyrken.newsapp2025.getApplicationComponent
import kz.nkaiyrken.newsapp2025.presentation.NewsCategory
import kz.nkaiyrken.newsapp2025.presentation.ScrollingSecondaryTab
import kz.nkaiyrken.newsapp2025.ui.utils.formatDateString

@Composable
fun NewsListScreen(
    paddings: PaddingValues,
    onArticleClick: (Article) -> Unit,
) {

    val component = getApplicationComponent()
    val viewModel: NewsListViewModel = viewModel(factory = component.getViewModelFactory())
    val screenState = viewModel.screenState.collectAsState(NewsListScreenState.Initial)

    Box(
        modifier = Modifier.padding(paddings)
    ) {

        when (val currentScreenState = screenState.value) {
            is NewsListScreenState.Initial -> {
                NewsListScreenErrorContent()
            }
            is NewsListScreenState.Loading -> {
                NewsListScreenLoadingContent()
            }

            is NewsListScreenState.Data -> {
                NewsListScreenContent(
                    viewModel = viewModel,
                    screenState = currentScreenState,
                    onArticleClick = onArticleClick,
                    paddings = paddings
                )
            }
        }
    }
}

@Composable
fun NewsListScreenContent(
    viewModel: NewsListViewModel,
    screenState: NewsListScreenState.Data,
    onArticleClick: (Article) -> Unit,
    paddings: PaddingValues,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        val categories = listOf(
            NewsCategory.Sport,
            NewsCategory.Business,
            NewsCategory.Science,
            NewsCategory.Health,
            NewsCategory.Entertainment,
            NewsCategory.Technology
        )

        ScrollingSecondaryTab(
            categories = categories,
            selectedCategory = screenState.selectedCategory,
            onCategorySelected = { viewModel.setCategory(it) }
        )

        HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outline)

        NewsList(
            viewModel = viewModel,
            onArticleClick = onArticleClick,
            newsList = screenState.articles,
            nextDataIsLoading = screenState.nextDataIsLoading,
            isLastPage = screenState.isLastPage,
            isLoadingFailed = screenState.isLoadingFailed,
        )
    }
}

@Composable
fun NewsList(
    viewModel: NewsListViewModel,
    onArticleClick: (Article) -> Unit,
    newsList: List<Article>,
    nextDataIsLoading: Boolean,
    isLastPage: Boolean,
    isLoadingFailed: Boolean,
) {

    if (newsList.isEmpty() && isLoadingFailed) {
        // Если список пустой, показываем соответствующее сообщение
        NewsListScreenErrorContent()
        return
    }

    val listState = rememberLazyListState()

    // Отслеживаем индекс последнего видимого элемента для подгрузки следующей страницы
    LaunchedEffect(listState, newsList.size, nextDataIsLoading, isLastPage) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .filterNotNull()
            .distinctUntilChanged()
            .collect { lastVisibleIndex ->
                if (lastVisibleIndex >= newsList.size - 1 && !nextDataIsLoading && !isLastPage) {
                    viewModel.loadNextNews()
                }
            }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant),
    ) {
        items(
            items = newsList,
            key = { it.url }
        ) { newsItem ->
            NewsCardItem(
                item = newsItem,
                onClick = { onArticleClick(newsItem) }
            )
        }
        item {
            NewsListFooter(
                isLastPage = isLastPage,
                isLoading = nextDataIsLoading,
                isLoadingFailed = isLoadingFailed,
                onRetryClick = { /* ... */ }
            )
        }
    }
}

@Composable
fun NewsListScreenErrorContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.no_news_found),
            modifier = Modifier.padding(16.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun NewsListFooter(
    isLastPage: Boolean,
    isLoading: Boolean,
    isLoadingFailed: Boolean,
    onRetryClick: () -> Unit,
) {
    when {
        isLastPage -> Text(
            text = stringResource(R.string.you_have_seen_all_the_news),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        isLoading -> Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }

        isLoadingFailed -> TextButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            onClick = onRetryClick
        ) {
            Text(stringResource(R.string.retry))
        }
    }
}


@Composable
fun NewsCardItem(
    item: Article,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            if (item.imageUrl != null) {
                AsyncImage(
                    model = item.imageUrl,
                    contentDescription = stringResource(R.string.news_image),
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(android.R.drawable.ic_menu_report_image),
                    error = painterResource(android.R.drawable.ic_menu_report_image)
                )
                Spacer(modifier = Modifier.width(12.dp))
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = formatDateString(
                            context = LocalContext.current,
                            dateInfo = item.publishedAt,
                        ),
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = item.sourceName.toString(),
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        maxLines = 1,
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun NewsListPreview() {
    NewsList(
        onArticleClick = {},
        newsList = List(10) {
            Article(
                sourceId = "id",
                sourceName = "name",
                title = "title",
                description = "description",
                author = "author",
                url = "url",
                imageUrl = "imageUrl",
                publishedAt = DateInfo.Today("12:00"),
                content = "content",
            )
        },
        nextDataIsLoading = false,
        viewModel = viewModel(),
        isLastPage = false,
        isLoadingFailed = false,
    )
}

@Composable
fun NewsListScreenLoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
@Preview
fun NewsCardItemPreview() {
    NewsCardItem(
        item = Article(
            sourceId = "id",
            sourceName = "name",
            title = "title",
            description = "description",
            author = "author",
            url = "url",
            imageUrl = "imageUrl",
            publishedAt = DateInfo.Today("12:00"),
            content = "content",
        ),
        onClick = {}
    )
}