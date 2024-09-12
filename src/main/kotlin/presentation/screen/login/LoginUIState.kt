package presentation.screen.login

import androidx.compose.runtime.Immutable

@Immutable
data class LoginUIState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isUserError: ErrorWrapper? = null,
    val isPasswordError: ErrorWrapper? = null,
    val isEnable: Boolean = false,
    val snackBarTitle: String? = null,
    val isSnackBarVisible: Boolean = false,
    val isKeepLogin: Boolean = false,
)

@Immutable
data class ErrorWrapper(
    val errorMessage: String = "",
    val isError: Boolean = false
)