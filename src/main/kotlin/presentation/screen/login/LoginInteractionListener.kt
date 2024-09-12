package presentation.screen.login

interface LoginInteractionListener {
    fun onPasswordChange(password: String)

    fun onUsernameChange(username: String)

    fun onKeepMeLoggedInChange(keepMeLoggedIn: Boolean)

    fun onLoginClicked()

    fun onSnackBarDismiss()
}