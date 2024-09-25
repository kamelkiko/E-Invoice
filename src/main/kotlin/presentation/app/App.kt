package presentation.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import design_system.theme.Theme
import presentation.screen.splash.SplashScreen
import resource.EInvoiceTheme

@Composable
internal fun App() {
    MainApp.Content()
}

object MainApp : Screen {

    @Composable
    override fun Content() {

        val appViewModel = koinScreenModel<AppViewModel>()
        val userLanguage by appViewModel.state.collectAsState()

        EInvoiceTheme(languageCode = userLanguage) {
//            LudoGame()
            Box(
                Modifier.fillMaxSize().background(Theme.colors.background),
            ) {
                Navigator(SplashScreen) {
                    SlideTransition(it)
                }
            }
        }
    }
}