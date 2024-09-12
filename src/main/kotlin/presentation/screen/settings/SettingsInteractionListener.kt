package presentation.screen.settings

interface SettingsInteractionListener {
    fun onPasswordChange(password: String)

    fun onUsernameChange(username: String)

    fun onUpdateClicked()

    fun onSnackBarDismiss()

    fun onLanguageChange(language: String)

    fun onStoreSelected(id: Int)
}