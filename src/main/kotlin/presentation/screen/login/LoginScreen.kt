package presentation.screen.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import com.abapps.e_invoice.generated.resources.Res
import com.abapps.e_invoice.generated.resources.bg
import com.abapps.e_invoice.generated.resources.ic_info_square
import design_system.composable.EIButton
import design_system.composable.EICheckBox
import design_system.composable.EITextField
import design_system.theme.Theme
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.vectorResource
import presentation.screen.composable.SnackBar
import presentation.screen.main.MainContainer
import presentation.util.EventHandler
import presentation.util.kms
import resource.Resources

class LoginScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val screenModel = koinScreenModel<LoginViewModel>()
        val state by screenModel.state.collectAsState()
        val listener: LoginInteractionListener = screenModel

        EventHandler(screenModel.effect) { effect, navigator ->
            when (effect) {
                is LoginUIEffect.LoginSuccess -> navigator.replaceAll(MainContainer)

                is LoginUIEffect.LoginFailed -> {}
            }
        }

        LaunchedEffect(state.isSnackBarVisible) {
            delay(1500)
            listener.onSnackBarDismiss()
        }

        Row(
            Modifier.background(Theme.colors.surface).fillMaxSize()
                .padding(
                    top = 40.kms,
                    start = 40.kms,
                    bottom = 40.kms
                ),
            horizontalArrangement = Arrangement.Center
        ) {
            Box(Modifier.weight(1f)) {
                Image(
                    painter = painterResource(Res.drawable.bg),
                    contentDescription = null,
                    alignment = Alignment.CenterStart,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                        .clip(RoundedCornerShape(Theme.radius.large))
                )
            }
            Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.Bottom,
                ) {
                    AnimatedVisibility(
                        state.isSnackBarVisible,
                        modifier = Modifier.animateContentSize(),
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        SnackBar(
                            onDismiss = listener::onSnackBarDismiss,
                            backgroundColor = Theme.colors.hover
                        ) {
                            Image(
                                imageVector = vectorResource(Res.drawable.ic_info_square),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(color = Theme.colors.primary),
                                modifier = Modifier.padding(16.kms)
                            )
                            Text(
                                text = state.snackBarTitle ?: Resources.strings.noInternet,
                                style = Theme.typography.titleMedium,
                                color = Theme.colors.primary,
                            )
                        }
                    }
                }
                Column(
                    Modifier.fillMaxHeight().width(450.kms),
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        Resources.strings.login,
                        style = Theme.typography.headlineLarge,
                        color = Theme.colors.contentPrimary
                    )
                    Text(
                        Resources.strings.loginTitle,
                        style = Theme.typography.titleMedium,
                        color = Theme.colors.contentTertiary,
                        modifier = Modifier.padding(top = 8.kms)
                    )
                    EITextField(
                        onValueChange = listener::onUsernameChange,
                        text = state.username,
                        label = Resources.strings.loginUsername,
                        modifier = Modifier.padding(top = 40.kms),
                        errorMessage = state.isUserError?.errorMessage ?: "",
                        isError = state.isUserError?.isError ?: false,
                        keyboardActions = KeyboardActions(
                            onDone = {
                                if (state.isEnable) listener.onLoginClicked()
                            }
                        ),
                        imeAction = ImeAction.Done
                    )
                    EITextField(
                        onValueChange = listener::onPasswordChange,
                        text = state.password,
                        label = Resources.strings.loginPassword,
                        keyboardType = KeyboardType.Password,
                        modifier = Modifier.padding(top = 16.kms),
                        errorMessage = state.isPasswordError?.errorMessage ?: "",
                        isError = state.isPasswordError?.isError ?: false,
                        keyboardActions = KeyboardActions(
                            onDone = {
                                if (state.isEnable) listener.onLoginClicked()
                            }
                        ),
                        imeAction = ImeAction.Done
                    )
                    EICheckBox(
                        Resources.strings.loginKeepMeLoggedIn,
                        listener::onKeepMeLoggedInChange,
                        isChecked = state.isKeepLogin,
                        modifier = Modifier.padding(top = 24.kms)
                    )
                    EIButton(
                        title = Resources.strings.loginButton,
                        onClick = { listener.onLoginClicked() },
                        modifier = Modifier.padding(top = 24.kms).fillMaxWidth(),
                        enabled = state.isEnable,
                        isLoading = state.isLoading,
                    )
                }
            }
        }
    }
}