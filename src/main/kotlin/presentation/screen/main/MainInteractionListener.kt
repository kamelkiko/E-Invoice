package presentation.screen.main

interface MainInteractionListener {
    fun onClickDropDownMenu()
    fun onDismissDropDownMenu()
    fun onClickLogout()
    fun onClickActive()
    fun onSwitchTheme()
    fun onSnackBarDismiss()
    fun onSnackBarSuccessDismiss()
}