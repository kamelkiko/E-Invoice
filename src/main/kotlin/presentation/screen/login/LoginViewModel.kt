package presentation.screen.login

import cafe.adriel.voyager.core.model.screenModelScope
import domain.usecase.GetIsLoggedInUseCase
import domain.usecase.LoginUserUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import presentation.base.BaseViewModel
import presentation.base.ErrorState

class LoginViewModel(
    private val login: LoginUserUseCase,
    private val getIsLoggedIn: GetIsLoggedInUseCase,
) : BaseViewModel<LoginUIState, LoginUIEffect>(LoginUIState()),
    LoginInteractionListener {
    override val viewModelScope: CoroutineScope get() = screenModelScope

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val isLoggedIn = getIsLoggedIn.invoke()
            updateState { it.copy(isKeepLogin = isLoggedIn) }
        }
    }

    override fun onPasswordChange(password: String) {
        updateState { it.copy(password = password) }
        updateLoginClickedState()
    }

    override fun onUsernameChange(username: String) {
        updateState { it.copy(username = username) }
        updateLoginClickedState()
    }

    override fun onKeepMeLoggedInChange(keepMeLoggedIn: Boolean) {
        updateState { it.copy(isKeepLogin = keepMeLoggedIn) }
    }

    private fun updateLoginClickedState() {
        updateState { it.copy(isEnable = (state.value.username.isNotBlank() && state.value.password.isNotBlank()) || state.value.password.length == 6) }
    }

    override fun onLoginClicked() {
        clearState()
        updateState { it.copy(isLoading = true, isEnable = false) }
        state.value.apply {
            tryToExecute(
                { login.loginUser(username = username, password = password, isKeepLogin = isKeepLogin) },
                { onLoginSuccess() },
                ::onError
            )
        }
    }

    private fun onLoginSuccess() {
        updateState { it.copy(isLoading = false) }
        sendNewEffect(LoginUIEffect.LoginSuccess)
    }

    private fun onError(error: ErrorState) {
        updateState { it.copy(isLoading = false, isEnable = true) }
        handleErrorState(error)
    }

    private fun handleErrorState(error: ErrorState) {
        when (error) {
            is ErrorState.UnknownError -> updateState {
                it.copy(
                    isSnackBarVisible = true,
                    snackBarTitle = error.message,
                )
            }

            is ErrorState.UnAuthorized -> updateState {
                it.copy(
                    isSnackBarVisible = true,
                    snackBarTitle = error.message,
                )
            }

            else -> updateState { it.copy(isSnackBarVisible = true, snackBarTitle = null) }
        }
    }

    override fun onSnackBarDismiss() {
        updateState {
            it.copy(
                isSnackBarVisible = false,
            )
        }
    }


    private fun clearState() {
        updateState {
            it.copy(
                isLoading = false,
                isPasswordError = ErrorWrapper(),
                isUserError = ErrorWrapper(),
                isSnackBarVisible = false,
                snackBarTitle = null,
            )
        }
    }
}