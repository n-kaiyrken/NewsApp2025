package kz.nkaiyrken.newsapp2025.presentation.profile

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onNavigateBack: () -> Unit,
) {
    val screenState by viewModel.screenState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Профиль",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Открыть настройки */ }) {
                        Icon(Icons.Default.Settings, contentDescription = "Настройки")
                    }
                }
            )
        }
    ) { paddingValues ->
        when (screenState) {
            is ProfileScreenState.Initial -> {
                ProfileScreenLoadingContent(
                    modifier = Modifier.padding(paddingValues)
                )
            }
            is ProfileScreenState.Loading -> {
                ProfileScreenLoadingContent(
                    modifier = Modifier.padding(paddingValues)
                )
            }
            is ProfileScreenState.Success -> {
                ProfileScreenContent(
                    screenState = screenState as ProfileScreenState.Success,
                    onEditProfileClick = { /* TODO: Редактировать профиль */ },
                    onNotificationsClick = { /* TODO: Открыть уведомления */ },
                    onPaymentMethodsClick = { /* TODO: Открыть способы оплаты */ },
                    onAccountSettingsClick = { /* TODO: Открыть настройки аккаунта */ },
                    onTransactionHistoryClick = { /* TODO: Открыть историю транзакций */ },
                    onHelpClick = { /* TODO: Открыть FAQ */ },
                    onLogoutClick = { viewModel.logout() },
                    modifier = Modifier.padding(paddingValues)
                )
            }
            is ProfileScreenState.Error -> {
                ProfileScreenErrorContent(
                    message = (screenState as ProfileScreenState.Error).message,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}
