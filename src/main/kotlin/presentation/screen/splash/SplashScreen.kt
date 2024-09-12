package presentation.screen.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ColorFilter
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.abapps.e_invoice.generated.resources.Res
import com.abapps.e_invoice.generated.resources.ic_cyclone
import design_system.theme.Theme
import domain.usecase.GetIsLoggedInUseCase
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.vectorResource
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import presentation.screen.login.LoginScreen
import presentation.screen.main.MainContainer

object SplashScreen : Screen, KoinComponent {
    val isLoggedIn: GetIsLoggedInUseCase by inject()

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        LaunchedEffect(Unit) {
            delay(1500)
            if (isLoggedIn.invoke())
                navigator.replace(MainContainer)
            else
                navigator.replace(LoginScreen())
        }

        Surface(
            Modifier.fillMaxSize(),
            color = Theme.colors.background
        ) {
            Box(contentAlignment = Alignment.Center) {

                val isAnimate by remember { mutableStateOf(true) }
                val transition = rememberInfiniteTransition()
                val rotate by transition.animateFloat(
                    initialValue = 0f,
                    targetValue = 360f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1000, easing = LinearEasing)
                    )
                )

                Image(
                    imageVector = vectorResource(Res.drawable.ic_cyclone),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(Theme.colors.contentPrimary),
                    modifier = Modifier.fillMaxSize(0.4f)
                        .run { if (isAnimate) rotate(rotate) else this },
                )
            }
        }
    }
}