package presentation.screen.setup

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
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
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import com.abapps.e_invoice.generated.resources.Res
import com.abapps.e_invoice.generated.resources.ic_info_square
import design_system.composable.EIButton
import design_system.composable.EITextField
import design_system.theme.Theme
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.vectorResource
import presentation.screen.composable.SnackBar
import presentation.screen.settings.SettingsUIEffect
import presentation.util.EventHandler
import presentation.util.kms
import resource.Resources

class SetupScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val setupViewModel: SetupViewModel = koinScreenModel()
        val state by setupViewModel.state.collectAsState()
        EventHandler(setupViewModel.effect) { effect, _ ->
            when (effect) {
                SetupUIEffect.SetupSuccess -> {

                }
            }
        }

        LaunchedEffect(state.isSnackBarVisible) {
            delay(1500)
            setupViewModel.onSnackBarDismiss()
        }

        Column(
            Modifier.background(Theme.colors.surface).fillMaxSize().padding(horizontal = 40.kms)
                .verticalScroll(rememberScrollState())
        ) {
            EITextField(
                onValueChange = setupViewModel::onRinChange,
                text = state.setupStore.rin,
                label = "RIN",
                modifier = Modifier.padding(top = 40.kms),
            )
            EITextField(
                onValueChange = setupViewModel::onCompanyTradeNameChange,
                text = state.setupStore.companyTradeName,
                label = "Company Trade Name",
                modifier = Modifier.padding(top = 16.kms),
            )
            EITextField(
                onValueChange = setupViewModel::onBranchCodeChange,
                text = state.setupStore.branchCode,
                label = "Branch Code",
                modifier = Modifier.padding(top = 40.kms),
            )
            EITextField(
                onValueChange = setupViewModel::onCountryChange,
                text = state.setupStore.country,
                label = "Country",
                modifier = Modifier.padding(top = 16.kms),
            )
            EITextField(
                onValueChange = setupViewModel::onGovernanceChange,
                text = state.setupStore.governance,
                label = "Governance",
                modifier = Modifier.padding(top = 40.kms),
            )
            EITextField(
                onValueChange = setupViewModel::onRegionCityChange,
                text = state.setupStore.regionCity,
                label = "Region City",
                modifier = Modifier.padding(top = 16.kms),
            )
            EITextField(
                onValueChange = setupViewModel::onStreetChange,
                text = state.setupStore.street,
                label = "Street",
                modifier = Modifier.padding(top = 16.kms),
            )
            EITextField(
                onValueChange = setupViewModel::onBuildingNumberChange,
                text = state.setupStore.buildingNumber,
                label = "Building Number",
                modifier = Modifier.padding(top = 16.kms),
            )
            EITextField(
                onValueChange = setupViewModel::onPostalCodeChange,
                text = state.setupStore.postalCode,
                label = "Postal Code",
                modifier = Modifier.padding(top = 16.kms),
            )
            EITextField(
                onValueChange = setupViewModel::onFloorChange,
                text = state.setupStore.floor,
                label = "Floor",
                modifier = Modifier.padding(top = 16.kms),
            )
            EITextField(
                onValueChange = setupViewModel::onRoomChange,
                text = state.setupStore.room,
                label = "Room",
                modifier = Modifier.padding(top = 16.kms),
            )
            EITextField(
                onValueChange = setupViewModel::onLandmarkChange,
                text = state.setupStore.landmark,
                label = "Landmark",
                modifier = Modifier.padding(top = 16.kms),
            )
            EITextField(
                onValueChange = setupViewModel::onAdditionalInformationChange,
                text = state.setupStore.additionalInformation,
                label = "Additional Information",
                modifier = Modifier.padding(top = 16.kms),
            )
            EITextField(
                onValueChange = setupViewModel::onDeviceSerialNumberChange,
                text = state.setupStore.deviceSerialNumber,
                label = "Device Serial Number",
                modifier = Modifier.padding(top = 16.kms),
            )
            EITextField(
                onValueChange = setupViewModel::onSyndicateLicenseNumberChange,
                text = state.setupStore.syndicateLicenseNumber,
                label = "Syndicate License Number",
                modifier = Modifier.padding(top = 16.kms),
            )
            EITextField(
                onValueChange = setupViewModel::onActivityCodeChange,
                text = state.setupStore.activityCode,
                label = "Activity Code",
                modifier = Modifier.padding(top = 16.kms),
            )
            EITextField(
                onValueChange = setupViewModel::onPosSerialChange,
                text = state.setupStore.posSerial,
                label = "POS Serial",
                modifier = Modifier.padding(top = 16.kms),
            )
            EITextField(
                onValueChange = setupViewModel::onPosOsVersionChange,
                text = state.setupStore.posOsVersion,
                label = "POS OS Version",
                modifier = Modifier.padding(top = 16.kms),
            )
            EITextField(
                onValueChange = setupViewModel::onClientIdChange,
                text = state.setupStore.clientId,
                label = "Client ID",
                modifier = Modifier.padding(top = 16.kms),
            )
            EITextField(
                onValueChange = setupViewModel::onClientSecretChange,
                text = state.setupStore.clientSecret,
                label = "Client Secret",
                modifier = Modifier.padding(top = 16.kms)
            )
            EIButton(
                title = Resources.strings.update,
                onClick = { setupViewModel.onUpdateClicked() },
                modifier = Modifier.padding(top = 24.kms).fillMaxWidth(),
                isLoading = state.isLoading,
            )
            Spacer(Modifier.height(40.kms))
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
                        onDismiss = setupViewModel::onSnackBarDismiss,
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