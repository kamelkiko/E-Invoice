package presentation.screen.setup

interface SetupInteractionListener {
    fun onRinChange(rin: String)
    fun onCompanyTradeNameChange(name: String)
    fun onBranchCodeChange(code: String)
    fun onCountryChange(country: String)
    fun onGovernanceChange(governance: String)
    fun onRegionCityChange(regionCity: String)
    fun onStreetChange(street: String)
    fun onBuildingNumberChange(buildingNumber: String)
    fun onPostalCodeChange(postalCode: String)
    fun onFloorChange(floor: String)
    fun onRoomChange(room: String)
    fun onLandmarkChange(landmark: String)
    fun onAdditionalInformationChange(additionalInfo: String)
    fun onDeviceSerialNumberChange(deviceSerialNumber: String)
    fun onSyndicateLicenseNumberChange(syndicateLicenseNumber: String)
    fun onActivityCodeChange(activityCode: String)
    fun onPosSerialChange(posSerial: String)
    fun onPosOsVersionChange(posOsVersion: String)
    fun onClientIdChange(clientId: String)
    fun onClientSecretChange(clientSecret: String)

    // Additional interactions
    fun onUpdateClicked()
    fun onSnackBarDismiss()
}