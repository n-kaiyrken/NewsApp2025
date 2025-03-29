package kz.nkaiyrken.newsapp2025.presentation.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kz.nkaiyrken.newsapp2025.R
import kz.nkaiyrken.newsapp2025.presentation.profile.components.EditProfileButton
import kz.nkaiyrken.newsapp2025.presentation.profile.components.ProfileAvatar
import kz.nkaiyrken.newsapp2025.presentation.profile.components.ProfileMenuItem
import kz.nkaiyrken.newsapp2025.presentation.profile.components.StatsCard

@Composable
fun ProfileScreenContent(
    screenState: ProfileScreenState.Success,
    onEditProfileClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onPaymentMethodsClick: () -> Unit,
    onAccountSettingsClick: () -> Unit,
    onTransactionHistoryClick: () -> Unit,
    onHelpClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        
        // Аватар
        ProfileAvatar(avatarUrl = screenState.profile.avatarUrl)

        Spacer(modifier = Modifier.height(16.dp))

        // Имя пользователя
        Text(
            text = screenState.profile.name,
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Email
        Text(
            text = screenState.profile.email,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Кнопка редактирования
        EditProfileButton(onClick = onEditProfileClick)

        Spacer(modifier = Modifier.height(24.dp))

        // Статистика
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StatsCard(
                title = "Заказы",
                value = screenState.stats.ordersCount.toString(),
                modifier = Modifier.weight(1f)
            )
            StatsCard(
                title = "Избранное",
                value = screenState.stats.favoritesCount.toString(),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Меню профиля
        Column {
            ProfileMenuItem(
                icon = Icons.Outlined.Notifications,
                title = stringResource(R.string.notifications),
                onClick = onNotificationsClick
            )
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                thickness = 0.5.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )
            ProfileMenuItem(
                icon = Icons.Outlined.ShoppingCart,
                title = stringResource(R.string.payment_methods),
                onClick = onPaymentMethodsClick
            )
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                thickness = 0.5.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )
            ProfileMenuItem(
                icon = Icons.Outlined.Settings,
                title = stringResource(R.string.accounts_settings),
                onClick = onAccountSettingsClick
            )
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                thickness = 0.5.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )
            ProfileMenuItem(
                icon = Icons.Outlined.DateRange,
                title = stringResource(R.string.transaction_history),
                onClick = onTransactionHistoryClick
            )
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                thickness = 0.5.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )
            ProfileMenuItem(
                icon = Icons.Outlined.Info,
                title = stringResource(R.string.faq),
                onClick = onHelpClick
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Кнопка выхода
        Button(
            onClick = onLogoutClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Icon(
                Icons.Default.ExitToApp,
                contentDescription = null,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(stringResource(R.string.exit))
        }
    }
}

@Composable
fun ProfileScreenLoadingContent(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ProfileScreenErrorContent(
    message: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error
        )
    }
} 