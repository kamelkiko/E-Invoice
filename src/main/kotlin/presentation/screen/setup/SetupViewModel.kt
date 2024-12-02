package presentation.screen.setup

import cafe.adriel.voyager.core.model.screenModelScope
import domain.usecase.ManageSetupStoreUseCase
import kotlinx.coroutines.CoroutineScope
import presentation.base.BaseViewModel
import presentation.base.ErrorState

class SetupViewModel(
    private val manageSetupStoreUseCase: ManageSetupStoreUseCase,
) : BaseViewModel<SetupUIState, SetupUIEffect>(SetupUIState()),
    SetupInteractionListener {
    override val viewModelScope: CoroutineScope get() = screenModelScope

    init {
        getSetup()
    }

    private fun getSetup() {
        updateState { it.copy(isEnable = false) }
        state.value.apply {
            tryToExecute(
                { manageSetupStoreUseCase.getSetup() },
                { setup ->
                    updateState {
                        it.copy(
                            isEnable = true,
                            setupStore = setup.toSetupState(),
                        )
                    }
                },
                ::onError
            )
        }
    }

    override fun onRinChange(rin: String) {
        updateState { it.copy(setupStore = it.setupStore.copy(rin = rin)) }
    }

    override fun onCompanyTradeNameChange(name: String) {
        updateState { it.copy(setupStore = it.setupStore.copy(companyTradeName = name)) }
    }

    override fun onBranchCodeChange(code: String) {
        updateState { it.copy(setupStore = it.setupStore.copy(branchCode = code)) }
    }

    override fun onCountryChange(country: String) {
        updateState { it.copy(setupStore = it.setupStore.copy(country = country)) }
    }

    override fun onGovernanceChange(governance: String) {
        updateState { it.copy(setupStore = it.setupStore.copy(governance = governance)) }
    }

    override fun onRegionCityChange(regionCity: String) {
        updateState { it.copy(setupStore = it.setupStore.copy(regionCity = regionCity)) }
    }

    override fun onStreetChange(street: String) {
        updateState { it.copy(setupStore = it.setupStore.copy(street = street)) }
    }

    override fun onBuildingNumberChange(buildingNumber: String) {
        updateState { it.copy(setupStore = it.setupStore.copy(buildingNumber = buildingNumber)) }
    }

    override fun onPostalCodeChange(postalCode: String) {
        updateState { it.copy(setupStore = it.setupStore.copy(postalCode = postalCode)) }
    }

    override fun onFloorChange(floor: String) {
        updateState { it.copy(setupStore = it.setupStore.copy(floor = floor)) }
    }

    override fun onRoomChange(room: String) {
        updateState { it.copy(setupStore = it.setupStore.copy(room = room)) }
    }

    override fun onLandmarkChange(landmark: String) {
        updateState { it.copy(setupStore = it.setupStore.copy(landmark = landmark)) }
    }

    override fun onAdditionalInformationChange(additionalInfo: String) {
        updateState { it.copy(setupStore = it.setupStore.copy(additionalInformation = additionalInfo)) }
    }

    override fun onDeviceSerialNumberChange(deviceSerialNumber: String) {
        updateState { it.copy(setupStore = it.setupStore.copy(deviceSerialNumber = deviceSerialNumber)) }
    }

    override fun onSyndicateLicenseNumberChange(syndicateLicenseNumber: String) {
        updateState { it.copy(setupStore = it.setupStore.copy(syndicateLicenseNumber = syndicateLicenseNumber)) }
    }

    override fun onActivityCodeChange(activityCode: String) {
        updateState { it.copy(setupStore = it.setupStore.copy(activityCode = activityCode)) }
    }

    override fun onPosSerialChange(posSerial: String) {
        updateState { it.copy(setupStore = it.setupStore.copy(posSerial = posSerial)) }
    }

    override fun onPosOsVersionChange(posOsVersion: String) {
        updateState { it.copy(setupStore = it.setupStore.copy(posOsVersion = posOsVersion)) }
    }

    override fun onClientIdChange(clientId: String) {
        updateState { it.copy(setupStore = it.setupStore.copy(clientId = clientId)) }
    }

    override fun onClientSecretChange(clientSecret: String) {
        updateState { it.copy(setupStore = it.setupStore.copy(clientSecret = clientSecret)) }
    }

    override fun onUpdateClicked() {
        clearState()
        updateState {
            it.copy(
                isLoading = true,
                isEnable = false,
                isSnackBarVisible = false,
                snackBarTitle = null,
            )
        }
        state.value.apply {
            tryToExecute(
                {
//                    if (state.value.isCreatedStore)
//                        manageSetupStoreUseCase.createSetup(state.value.setupStore.toSetupStore(), 0)
//                    else
                    manageSetupStoreUseCase.createSetup(state.value.setupStore.toSetupStore(), 0)
                },
                { onUpdatedSuccess() },
                ::onError
            )
        }
    }

    private fun updateLoginClickedState() {
        // updateState { it.copy(isEnable = (state.value.username.isNotBlank() && state.value.password.isNotBlank())) }
    }

    private fun onUpdatedSuccess() {
        updateState {
            it.copy(
                isLoading = false,
                isCreatedStore = false,
                isSnackBarVisible = true,
                snackBarTitle = "Saved Success!",
                isEnable = true,
            )
        }
        sendNewEffect(SetupUIEffect.SetupSuccess)
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
                    isCreatedStore = true,
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