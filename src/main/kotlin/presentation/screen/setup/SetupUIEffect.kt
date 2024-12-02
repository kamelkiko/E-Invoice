package presentation.screen.setup

sealed interface SetupUIEffect {
    data object SetupSuccess : SetupUIEffect
}