package presentation.screen.settings

import cafe.adriel.voyager.core.model.screenModelScope
import data.util.AppConstants
import data.util.AppLanguage
import domain.entity.User
import domain.usecase.GetAllStoresUseCase
import domain.usecase.ManageUserUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import presentation.base.BaseViewModel
import presentation.base.ErrorState

class SettingsViewModel(
    private val manageUser: ManageUserUseCase,
    private val getAllStores: GetAllStoresUseCase,
) : BaseViewModel<SettingsUIState, SettingsUIEffect>(SettingsUIState()),
    SettingsInteractionListener {
    override val viewModelScope: CoroutineScope get() = screenModelScope

    init {
        getUser()
        getStores()
    }

    private fun getStores() {
        tryToExecute(
            function = { getAllStores() },
            onSuccess = { stores ->
                AppConstants.storeName = stores.firstOrNull()?.name ?: ""
                updateState {
                    it.copy(
                        stores = stores.map { store ->
                            SetupItemState(
                                id = store.id,
                                name = store.name
                            )
                        },
                        selectedStore = stores.first().let { store ->
                            SetupItemState(
                                id = store.id,
                                name = store.name
                            )
                        }
                    )
                }
            },
            onError = ::onError
        )
    }

    private fun getUser() {
        updateState { it.copy(isEnable = false) }
        state.value.apply {
            tryToExecute(
                { manageUser.getUser() },
                { user ->
                    updateState {
                        it.copy(
                            username = user.name,
                            password = user.password,
                            isEnable = true
                        )
                    }
                },
                ::onError
            )
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

    override fun onUpdateClicked() {
        clearState()
        updateState { it.copy(isLoading = true, isEnable = false) }
        state.value.apply {
            tryToExecute(
                {
                    if (state.value.isCreatedAccount)
                        manageUser.createUser(User(0, username, password), selectedStore.id)
                    else
                        manageUser.updateUser(username, password, selectedStore.id)
                },
                { onUpdatedSuccess() },
                ::onError
            )
        }
    }

    private fun updateLoginClickedState() {
        updateState { it.copy(isEnable = (state.value.username.isNotBlank() && state.value.password.isNotBlank())) }
    }

    private fun onUpdatedSuccess() {
        updateState {
            it.copy(
                isLoading = false,
                isCreatedAccount = false,
                isSnackBarVisible = true,
                snackBarTitle = null,
                isEnable = true
            )
        }
        sendNewEffect(SettingsUIEffect.SignupSuccess)
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

            is ErrorState.ValidationError -> updateState {
                it.copy(
                    isSnackBarVisible = true,
                    snackBarTitle = error.message,
                )
            }

            is ErrorState.NotFound -> updateState {
                it.copy(
                    isCreatedAccount = true,
                    isEnable = false,
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

    override fun onLanguageChange(language: String) {
        updateState { it.copy(language = language) }
        viewModelScope.launch(Dispatchers.IO) {
            AppLanguage.code.emit(language)
        }
    }

    override fun onStoreSelected(id: Int) {
        updateState { it.copy(selectedStore = it.selectedStore.copy(id = id)) }
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