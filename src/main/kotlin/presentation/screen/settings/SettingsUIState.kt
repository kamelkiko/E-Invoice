package presentation.screen.settings

import androidx.compose.runtime.Immutable
import util.LanguageCode

@Immutable
data class SettingsUIState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isUserError: ErrorWrapper? = null,
    val isPasswordError: ErrorWrapper? = null,
    val isEnable: Boolean = false,
    val snackBarTitle: String? = null,
    val isSnackBarVisible: Boolean = false,
    val isCreatedAccount: Boolean = false,
    val language: String = LanguageCode.EN.value,
    val stores: List<SetupItemState> = emptyList(),
    val selectedStore: SetupItemState = SetupItemState(),
)

@Immutable
data class SetupItemState(
    val id: Int = 0,
    val name: String = ""
)

@Immutable
data class ErrorWrapper(
    val errorMessage: String = "",
    val isError: Boolean = false
)