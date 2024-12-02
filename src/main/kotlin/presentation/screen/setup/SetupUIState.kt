package presentation.screen.setup

import androidx.compose.runtime.Immutable
import domain.entity.StoreSetup
import util.LanguageCode

@Immutable
data class SetupUIState(
    val setupStore: SetupStoreState = SetupStoreState(),
    val isLoading: Boolean = false,
    val isUserError: ErrorWrapper? = null,
    val isPasswordError: ErrorWrapper? = null,
    val isEnable: Boolean = false,
    val snackBarTitle: String? = null,
    val isSnackBarVisible: Boolean = false,
    val isCreatedStore: Boolean = false,
    val language: String = LanguageCode.EN.value,
)

@Immutable
data class SetupStoreState(
    val id: String = "",
    val rin: String = "",
    val companyTradeName: String = "",
    val branchCode: String = "",
    val country: String = "",
    val governance: String = "",
    val regionCity: String = "",
    val street: String = "",
    val buildingNumber: String = "",
    val postalCode: String = "",
    val floor: String = "",
    val room: String = "",
    val landmark: String = "",
    val additionalInformation: String = "",
    val deviceSerialNumber: String = "",
    val syndicateLicenseNumber: String = "",
    val activityCode: String = "",
    val posSerial: String = "",
    val posOsVersion: String = "",
    val clientId: String = "",
    val clientSecret: String = "",
)

@Immutable
data class ErrorWrapper(
    val errorMessage: String = "",
    val isError: Boolean = false
)


fun SetupStoreState.toSetupStore(): StoreSetup {
    return StoreSetup(
        id = id,
        rin = rin,
        companyTradeName = companyTradeName,
        branchCode = branchCode,
        country = country,
        governance = governance,
        regionCity = regionCity,
        street = street,
        buildingNumber = buildingNumber,
        postalCode = postalCode,
        floor = floor,
        room = room,
        landmark = landmark,
        additionalInformation = additionalInformation,
        deviceSerialNumber = deviceSerialNumber,
        syndicateLicenseNumber = syndicateLicenseNumber,
        activityCode = activityCode,
        posSerial = posSerial,
        posOsVersion = posOsVersion,
        clientId = clientId,
        clientSecret = clientSecret
    )
}

fun StoreSetup.toSetupState(): SetupStoreState {
    return SetupStoreState(
        id = id,
        rin = rin,
        companyTradeName = companyTradeName,
        branchCode = branchCode,
        country = country,
        governance = governance,
        regionCity = regionCity,
        street = street,
        buildingNumber = buildingNumber,
        postalCode = postalCode,
        floor = floor,
        room = room,
        landmark = landmark,
        additionalInformation = additionalInformation,
        deviceSerialNumber = deviceSerialNumber,
        syndicateLicenseNumber = syndicateLicenseNumber,
        activityCode = activityCode,
        posSerial = posSerial,
        posOsVersion = posOsVersion,
        clientId = clientId,
        clientSecret = clientSecret
    )
}