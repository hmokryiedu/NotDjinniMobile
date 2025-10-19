package not.djinni.presentation.screens.auth

import androidx.compose.runtime.Immutable

@Immutable
data class AuthState(
    val isLoading: Boolean = false,
)
