package presentation.screen.main

sealed interface MainUiEffect {
    data object Logout : MainUiEffect
}