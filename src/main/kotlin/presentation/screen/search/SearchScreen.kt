package presentation.screen.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.zIndex
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import com.abapps.e_invoice.generated.resources.Res
import com.abapps.e_invoice.generated.resources.ic_info_square
import com.abapps.e_invoice.generated.resources.ic_search
import design_system.composable.EIButton
import design_system.composable.EIOutlinedButton
import design_system.composable.EISimpleTextField
import design_system.theme.Theme
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import presentation.screen.composable.SnackBar
import presentation.screen.composable.modifier.cursorHoverIconHand
import presentation.screen.composable.table.EITable
import presentation.screen.receipts.ReceiptsTableFooter
import presentation.screen.receipts.TitleField
import presentation.util.kms
import resource.Resources
import kotlin.random.Random

class SearchScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val searchViewModel: SearchViewModel = koinScreenModel()
        val state by searchViewModel.state.collectAsState()

        LaunchedEffect(state.isSnackBarVisible) {
            delay(5000L)
            searchViewModel.onSnackBarDismiss()
        }
        LaunchedEffect(state.isSnackBarSuccessVisible) {
            delay(4000L)
            searchViewModel.onSnackBarSuccessDismiss()
        }

        Box(
            modifier = Modifier.background(Theme.colors.surface).padding(40.kms).fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Column(modifier = Modifier.fillMaxSize().align(Alignment.TopCenter)) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(bottom = 16.kms),
                    horizontalArrangement = Arrangement.spacedBy(16.kms),
                    verticalAlignment = Alignment.Top
                ) {
                    EISimpleTextField(
                        modifier = Modifier.widthIn(max = 540.kms),
                        hint = "Search by UUID",
                        onValueChange = searchViewModel::onSearchInputChange,
                        text = state.uuid,
                        keyboardType = KeyboardType.Text,
                        onTrailingIconClick = {},
                        trailingPainter = painterResource(Res.drawable.ic_search),
                        outlinedTextFieldDefaults = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Theme.colors.surface,
                            cursorColor = Theme.colors.contentTertiary,
                            errorCursorColor = Theme.colors.primary,
                            focusedBorderColor = Theme.colors.contentTertiary.copy(alpha = 0.2f),
                            unfocusedBorderColor = Theme.colors.contentBorder.copy(alpha = 0.1f),
                            errorBorderColor = Theme.colors.primary.copy(alpha = 0.5f),
                        )
                    )

                    EIButton(
                        title = Resources.strings.search,
                        onClick = searchViewModel::onClickSearch,
                        textPadding = PaddingValues(horizontal = 24.kms),
                        modifier = Modifier.cursorHoverIconHand()
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    EIOutlinedButton(
                        title = "Refresh",
                        onClick = searchViewModel::onRetry,
                        textPadding = PaddingValues(horizontal = 24.kms),
                        modifier = Modifier.cursorHoverIconHand()
                    )
                    EIButton(
                        title = "Import",
                        onClick = searchViewModel::onClickImport,
                        textPadding = PaddingValues(horizontal = 24.kms),
                        modifier = Modifier.cursorHoverIconHand(),
                        isLoading = state.isLoadingImport
                    )
                    EIButton(
                        title = "Export All",
                        onClick = searchViewModel::onClickExportAll,
                        textPadding = PaddingValues(horizontal = 24.kms),
                        modifier = Modifier.cursorHoverIconHand(),
                        isLoading = state.isLoadingExportAll
                    )
                }
                EITable(
                    hasConnection = state.hasConnection,
                    data = state.pageInfo.data,
                    key = { Random.nextInt(8) },
                    isVisible = state.hasConnection,
                    headers = state.tabHeader,
                    limitItem = state.specifiedReceipts,
                    isLoading = state.isLoading,
                    modifier = Modifier.fillMaxWidth(),
                    rowContent = { receipt ->
                        DetailsRow(
                            receipt = receipt,
                            position = state.pageInfo.data.indexOf(receipt) + 1,
                        )
                    },
                )
                ReceiptsTableFooter(
                    selectedPage = state.currentPage,
                    numberItemInPage = state.specifiedReceipts,
                    numberOfReceipts = state.pageInfo.numberOfReceipts,
                    pageCount = state.pageInfo.totalPages,
                    onPageClicked = searchViewModel::onPageClick,
                    onItemPerPageChanged = searchViewModel::onItemsIndicatorChange,
                )
            }
            AnimatedVisibility(
                visible = state.isSnackBarSuccessVisible,
                enter = fadeIn(initialAlpha = 0.3f),
                exit = fadeOut(targetAlpha = 0.3f)
            ) {
                SnackBar(
                    modifier = Modifier.zIndex(3f).align(Alignment.BottomCenter),
                    onDismiss = searchViewModel::onSnackBarSuccessDismiss
                ) {
                    Image(
                        painter = painterResource(Res.drawable.ic_info_square),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(color = Theme.colors.success),
                        modifier = Modifier.padding(16.kms)
                    )
                    Text(
                        text = state.snackBarSuccessTitle ?: "Done",
                        style = Theme.typography.titleMedium,
                        color = Theme.colors.success,
                    )
                }
            }
            AnimatedVisibility(
                visible = state.isSnackBarVisible,
                enter = fadeIn(initialAlpha = 0.3f),
                exit = fadeOut(targetAlpha = 0.3f)
            ) {
                SnackBar(
                    modifier = Modifier.zIndex(3f).align(Alignment.BottomCenter),
                    onDismiss = searchViewModel::onSnackBarDismiss
                ) {
                    Image(
                        painter = painterResource(Res.drawable.ic_info_square),
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
    }
}

@Composable
private fun RowScope.DetailsRow(
    position: Int,
    receipt: SearchDetailsUiState,
) {
    TitleField(
        text = position.toString(),
        color = Theme.colors.contentTertiary,
        weight = 0.5f
    )
    TitleField(text = receipt.receiptNumber, weight = 1f)
    TitleField(text = receipt.dateTimeIssued, weight = 2f)
    TitleField(text = receipt.uuid, weight = 2f)
    TitleField(text = receipt.submitUUID, weight = 2f)
    TitleField(text = receipt.status, weight = 0.5f)
}