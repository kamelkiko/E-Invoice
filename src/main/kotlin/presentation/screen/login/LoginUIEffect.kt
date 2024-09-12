package presentation.screen.login

sealed interface LoginUIEffect {
    data object LoginSuccess : LoginUIEffect

    data class LoginFailed(val errorMessage: String) : LoginUIEffect
}