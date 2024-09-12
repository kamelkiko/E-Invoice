package presentation.screen.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import com.abapps.e_invoice.generated.resources.Res
import com.abapps.e_invoice.generated.resources.ic_info_square
import design_system.composable.EIButton
import design_system.composable.EITextField
import design_system.theme.Theme
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.vectorResource
import presentation.screen.composable.DropDownState
import presentation.screen.composable.DropDownTextField
import presentation.screen.composable.SnackBar
import presentation.util.EventHandler
import presentation.util.kms
import resource.Resources
import util.LanguageCode

class SettingsScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val settingsViewModel: SettingsViewModel = koinScreenModel()
        val state by settingsViewModel.state.collectAsState()

        EventHandler(settingsViewModel.effect) { effect, _ ->
            when (effect) {
                SettingsUIEffect.SignupSuccess -> {

                }
            }
        }

        LaunchedEffect(state.isSnackBarVisible) {
            delay(1500)
            settingsViewModel.onSnackBarDismiss()
        }

        Column(
            Modifier.background(Theme.colors.surface).fillMaxSize().padding(40.kms)
        ) {
            Text(
                Resources.strings.manageAccount,
                style = Theme.typography.headlineLarge,
                color = Theme.colors.contentPrimary
            )
            Text(
                Resources.strings.editYourAccount,
                style = Theme.typography.titleMedium,
                color = Theme.colors.contentTertiary,
                modifier = Modifier.padding(top = 8.kms)
            )
            EITextField(
                onValueChange = settingsViewModel::onUsernameChange,
                text = state.username,
                label = Resources.strings.loginUsername,
                modifier = Modifier.padding(top = 40.kms),
                errorMessage = state.isPasswordError?.errorMessage ?: "",
                isError = state.isPasswordError?.isError ?: false,
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (state.isEnable) settingsViewModel.onUpdateClicked()
                    }
                ),
                imeAction = ImeAction.Done
            )
            EITextField(
                onValueChange = settingsViewModel::onPasswordChange,
                text = state.password,
                label = Resources.strings.loginPassword,
                keyboardType = KeyboardType.Password,
                modifier = Modifier.padding(top = 16.kms),
                errorMessage = state.isPasswordError?.errorMessage ?: "",
                isError = state.isPasswordError?.isError ?: false,
            )
            AnimatedVisibility(state.selectedStore.id != 0, modifier = Modifier.padding(top = 16.dp)) {
                DropDownTextField(
                    options = state.stores.map { store -> DropDownState(id = store.id, name = store.name) },
                    selectedItem = state.selectedStore.let { store ->
                        DropDownState(
                            id = store.id,
                            name = store.name
                        )
                    },
                    onClick = { settingsViewModel.onStoreSelected(it) },
                )
            }
            EIButton(
                title = Resources.strings.update,
                onClick = { settingsViewModel.onUpdateClicked() },
                modifier = Modifier.padding(top = 24.kms).fillMaxWidth(),
                enabled = state.isEnable,
                isLoading = state.isLoading,
            )
            Spacer(Modifier.height(40.kms))
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    Resources.strings.english,
                    style = Theme.typography.body,
                    color = Theme.colors.contentPrimary
                )
                RadioButton(
                    selected = state.language == LanguageCode.EN.value,
                    onClick = {
                        settingsViewModel.onLanguageChange(LanguageCode.EN.value)
                    },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Theme.colors.primary,
                        unselectedColor = Theme.colors.contentTertiary,
                    )
                )
                Text(
                    Resources.strings.arabic,
                    style = Theme.typography.body,
                    color = Theme.colors.contentPrimary
                )
                RadioButton(
                    selected = state.language == LanguageCode.AR.value,
                    onClick = {
                        settingsViewModel.onLanguageChange(LanguageCode.AR.value)
                    }, colors = RadioButtonDefaults.colors(
                        selectedColor = Theme.colors.primary,
                        unselectedColor = Theme.colors.contentTertiary,
                    )
                )
            }

            Spacer(Modifier.weight(1f))

            Column(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AnimatedVisibility(
                    state.isSnackBarVisible,
                    modifier = Modifier.animateContentSize(),
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    SnackBar(
                        onDismiss = settingsViewModel::onSnackBarDismiss,
                        backgroundColor = Theme.colors.hover
                    ) {
                        Image(
                            imageVector = vectorResource(Res.drawable.ic_info_square),
                            contentDescription = null,
                            colorFilter = if (state.snackBarTitle == null) ColorFilter.tint(color = Theme.colors.success)
                            else ColorFilter.tint(color = Theme.colors.primary),
                            modifier = Modifier.padding(16.kms)
                        )
                        Text(
                            text = state.snackBarTitle ?: Resources.strings.accountUpdated,
                            style = Theme.typography.titleMedium,
                            color = if (state.snackBarTitle == null) Theme.colors.success
                            else Theme.colors.primary,
                        )
                    }
                }
            }
        }
    }
}