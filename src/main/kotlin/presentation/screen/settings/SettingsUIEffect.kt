package presentation.screen.settings

sealed interface SettingsUIEffect {
    data object SignupSuccess : SettingsUIEffect
}