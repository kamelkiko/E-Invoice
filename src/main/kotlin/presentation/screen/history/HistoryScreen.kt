package presentation.screen.history

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.zIndex
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import com.abapps.e_invoice.generated.resources.Res
import com.abapps.e_invoice.generated.resources.ic_info_square
import design_system.composable.EIButton
import design_system.composable.EIOutlinedButton
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

class HistoryScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val historyViewModel: HistoryViewModel = koinScreenModel()
        val state by historyViewModel.state.collectAsState()

        LaunchedEffect(state.isSnackBarVisible) {
            delay(5000L)
            historyViewModel.onSnackBarDismiss()
        }
        LaunchedEffect(state.isSnackBarSuccessVisible) {
            delay(4000L)
            historyViewModel.onSnackBarSuccessDismiss()
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
                    Spacer(modifier = Modifier.weight(1f))
                    EIOutlinedButton(
                        title = "Refresh",
                        onClick = historyViewModel::onRetry,
                        textPadding = PaddingValues(horizontal = 24.kms),
                        modifier = Modifier.cursorHoverIconHand()
                    )
                    EIButton(
                        title = "Export All",
                        onClick = historyViewModel::onClickExportAll,
                        textPadding = PaddingValues(horizontal = 24.kms),
                        modifier = Modifier.cursorHoverIconHand(),
                        isLoading = state.isLoadingExportAll,
                        enabled = state.receipts.isNotEmpty()
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
                            onEditTaxiClicked = {
//                                historyViewModel.onEditTaxiClicked(
//                                    receipt.receiptNumber.toLongOrNull() ?: 0L
//                                )
                            }
                        )
                    },
                )
                ReceiptsTableFooter(
                    selectedPage = state.currentPage,
                    numberItemInPage = state.specifiedReceipts,
                    numberOfReceipts = state.pageInfo.numberOfReceipts,
                    pageCount = state.pageInfo.totalPages,
                    onPageClicked = historyViewModel::onPageClick,
                    onItemPerPageChanged = historyViewModel::onItemsIndicatorChange,
                )
            }
            AnimatedVisibility(
                visible = state.isSnackBarSuccessVisible,
                enter = fadeIn(initialAlpha = 0.3f),
                exit = fadeOut(targetAlpha = 0.3f)
            ) {
                SnackBar(
                    modifier = Modifier.zIndex(3f).align(Alignment.BottomCenter),
                    onDismiss = historyViewModel::onSnackBarSuccessDismiss
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
                    onDismiss = historyViewModel::onSnackBarDismiss
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
    receipt: HistoryDetailsUiState,
    onEditTaxiClicked: (Long) -> Unit,
) {
    TitleField(
        text = position.toString(),
        color = Theme.colors.contentTertiary,
        weight = 0.5f
    )
    //TitleField(text = receipt.id, weight = 2f)
    TitleField(text = receipt.receiptNumber, weight = 1f)
    //TitleField(text = receipt.totalAmount.toString(), weight = 1f)
    //TitleField(text = "2500", weight = 3f)
    //TitleField(text = receipt.taxTotals.toString())
//    TitleField(text = "EGP", weight = 1.5f)
    TitleField(text = receipt.dateTimeIssued, weight = 2f)
    TitleField(text = receipt.uuid ?: "", weight = 2f)
    TitleField(text = receipt.submitUUID ?: "", weight = 2f)
    TitleField(text = receipt.status ?: "", weight = 0.5f)
    //TitleField(text = receipt.storeName, weight = 2f)
//    Box(modifier = Modifier.weight(0.5f)) {
//        EICheckBox(
//            label = "",
//            onCheck = {
//                onEditTaxiClicked(receipt.receiptNumber.toLongOrNull() ?: 0L)
//                receipt.isChecked.not()
//            },
//            isChecked = receipt.isChecked
//        )
//        Image(
//            painter = painterResource(Res.drawable.ic_support),
//            contentDescription = null,
//            modifier = Modifier.noRippleEffect { /*onDropdownMenuClicked(taxi.id)*/ },
//            colorFilter = ColorFilter.tint(color = Theme.colors.contentPrimary)
//        )
//        EditTaxiDropdownMenu(
//            taxi = taxi,
//            onDropdownMenuDismiss = { onDropdownMenuDismiss(taxi.id) },
//            onEditTaxiClicked = onEditTaxiClicked,
//            onDeleteTaxiClicked = onDeleteTaxiClicked,
//        )
}