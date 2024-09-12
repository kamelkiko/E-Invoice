package presentation.screen.main

import androidx.compose.runtime.Immutable
import presentation.base.ErrorState

@Immutable
data class MainUiState(
    val username: String = "AbApps",
    val firstUsernameLetter: String = "A",
    val isLogin: Boolean = false,
    val error: ErrorState = ErrorState.UnknownError("Unknown error"),
    val isDropMenuExpanded: Boolean = false,
    val isDarkMode: Boolean = true,
    val hasInternetConnection: Boolean = true,
    val snackBarTitle: String? = null,
    val snackBarSuccessTitle: String? = null,
    val isSnackBarVisible: Boolean = false,
    val isSnackBarSuccessVisible: Boolean = false,
)