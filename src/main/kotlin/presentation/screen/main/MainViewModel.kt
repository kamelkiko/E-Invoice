package presentation.screen.main

import cafe.adriel.voyager.core.model.screenModelScope
import data.util.AppConstants
import domain.usecase.CreateTokenUseCase
import domain.usecase.LogoutUserUseCase
import domain.usecase.ManageUserUseCase
import kotlinx.coroutines.CoroutineScope
import presentation.base.BaseViewModel
import presentation.base.ErrorState

class MainViewModel(
    private val logout: LogoutUserUseCase,
    private val manageUser: ManageUserUseCase,
    private val createToken: CreateTokenUseCase,
) : BaseViewModel<MainUiState, MainUiEffect>(MainUiState()), MainInteractionListener {

    override val viewModelScope: CoroutineScope get() = screenModelScope

    init {
        getUserName()
    }

    private fun getUserName() {
        tryToExecute(
            function = manageUser::getUserName,
            onSuccess = { user ->
                updateState {
                    it.copy(
                        username = user,
                        firstUsernameLetter = user.first().toString()
                    )
                }
            },
            onError = ::onError
        )
    }

    private fun onError(error: ErrorState) {
        when (error) {
            is ErrorState.NetworkError -> {
                updateState { it.copy(error = error, hasInternetConnection = true) }
            }

            is ErrorState.UnAuthorized -> {
                updateState {
                    it.copy(
                        error = error, isSnackBarVisible = true,
                        snackBarTitle = error.message.toString()
                    )
                }
            }

            is ErrorState.ServerError -> {
                updateState {
                    it.copy(
                        error = error, isSnackBarVisible = true,
                        snackBarTitle = error.message.toString()
                    )
                }
            }

            is ErrorState.ValidationNetworkError -> {
                updateState {
                    it.copy(
                        error = error, isSnackBarVisible = true,
                        snackBarTitle = error.message.toString()
                    )
                }
            }

            is ErrorState.UnknownError -> {
                updateState {
                    it.copy(
                        error = error, isSnackBarVisible = true,
                        snackBarTitle = error.message.toString()
                    )
                }
            }

            is ErrorState.NotFound -> {
                updateState {
                    it.copy(
                        error = error,
                        isSnackBarVisible = true,
                        snackBarTitle = error.message.toString()
                    )
                }
            }

            is ErrorState.EmptyData -> {
                updateState {
                    it.copy(
                        error = error,
                        isSnackBarVisible = true,
                        snackBarTitle = error.message.toString()
                    )
                }
            }

            else -> {
                updateState {
                    it.copy(
                        error = error,
                        isSnackBarVisible = true,
                        snackBarTitle = "Something happened please try again!"
                    )
                }
            }
        }
    }

    override fun onClickDropDownMenu() {
        updateState { it.copy(isDropMenuExpanded = true) }
    }

    override fun onDismissDropDownMenu() {
        updateState { it.copy(isDropMenuExpanded = false) }
    }

    override fun onClickLogout() {
        tryToExecute(
            logout::logoutUser,
            { onLogoutSuccessfully() },
            ::onError
        )
    }

    override fun onClickActive() {
        if (AppConstants.storeId == -1) updateState {
            it.copy(
                error = ErrorState.UnknownError("Please choose store first"),
                isSnackBarVisible = true,
                snackBarTitle = "Please choose store first"
            )
        } else
            tryToExecute(
                function = { createToken.invoke(AppConstants.storeId) },
                onSuccess = {
                    updateState {
                        it.copy(isSnackBarSuccessVisible = true, snackBarSuccessTitle = "Activated done!")
                    }
                },
                onError = ::onError
            )
    }

    override fun onSwitchTheme() {
        updateState { it.copy(isDarkMode = !it.isDarkMode) }
    }

    override fun onSnackBarDismiss() {
        updateState { it.copy(isSnackBarVisible = false) }
    }

    override fun onSnackBarSuccessDismiss() {
        updateState { it.copy(isSnackBarSuccessVisible = false) }
    }

    private fun onLogoutSuccessfully() {
        updateState { it.copy(isLogin = false) }
        sendNewEffect(MainUiEffect.Logout)
    }

}