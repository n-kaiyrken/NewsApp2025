package kz.nkaiyrken.newsapp2025.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kz.nkaiyrken.newsapp2025.domain.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel @Inject constructor() : ViewModel() {
    private val _screenState = MutableStateFlow<ProfileScreenState>(ProfileScreenState.Initial)
    val screenState: StateFlow<ProfileScreenState> = _screenState.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _screenState.value = ProfileScreenState.Loading
            try {
                // TODO: Заменить на реальные данные из репозитория
                _screenState.value = ProfileScreenState.Success(
                    profile = UserProfile(
                        id = "1",
                        name = "Тестовый пользователь",
                        email = "test@example.com"
                    ),
                    stats = ProfileStats(
                        ordersCount = 13,
                        favoritesCount = 7
                    )
                )
            } catch (e: Exception) {
                _screenState.value = ProfileScreenState.Error(
                    message = e.message ?: "Неизвестная ошибка"
                )
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            // TODO: Реализовать выход из профиля
        }
    }
}