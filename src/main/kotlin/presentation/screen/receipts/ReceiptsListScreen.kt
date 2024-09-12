package presentation.screen.receipts

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.zIndex
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import com.abapps.e_invoice.generated.resources.Res
import com.abapps.e_invoice.generated.resources.ic_info_square
import com.abapps.e_invoice.generated.resources.ic_search
import design_system.composable.EIButton
import design_system.composable.EICheckBox
import design_system.composable.EIOutlinedButton
import design_system.composable.EISimpleTextField
import design_system.theme.Theme
import io.ktor.util.date.*
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import presentation.screen.composable.SnackBar
import presentation.screen.composable.modifier.cursorHoverIconHand
import presentation.screen.composable.table.EIPager
import presentation.screen.composable.table.EITable
import presentation.screen.composable.table.TotalItemsIndicator
import presentation.util.kms
import resource.Resources
import kotlin.random.Random

class ReceiptsListScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val receiptViewModel: ReceiptViewModel = koinScreenModel()
        val state by receiptViewModel.state.collectAsState()

        LaunchedEffect(state.isSnackBarVisible) {
            delay(5000L)
            receiptViewModel.onSnackBarDismiss()
        }
        LaunchedEffect(state.isSnackBarSuccessVisible) {
            delay(4000L)
            receiptViewModel.onSnackBarSuccessDismiss()
        }
//        val c = getData("Select * from dbo.Checks")
//        val b = c.map { row ->
//            convertRowToJson(
//                row.toString()
//            ).let {
//                println(
//                    it.replaceFirst("\"", "")
//                        .replace("{", "{\"")
//                        .replace("=", "\":\"")
//                        .replace(",", "\",\"")
//                        .replace("}", "\"}")
//                        .dropLast(1)
//                )
//                Json.decodeFromString<ResponseData>(
//                    it.replaceFirst("\"", "")
//                        .replace(" ", "")
//                        .replace("{", "{\"")
//                        .replace("=", "\":\"")
//                        .replace(",", "\",\"")
//                        .replace("}", "\"}")
//                        .dropLast(1)
//                )
//            }
//        }
//        println(b.toString())
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
                    val state1 = rememberDatePickerState()
                    AnimatedVisibility(state.dateFromIsVisible) {
                        DatePickerDialog(onDismissRequest = receiptViewModel::onDismissStartDate, {
                            EIButton("Done", onClick = {
                                receiptViewModel.onChooseStartDateInputChange(
                                    state1.selectedDateMillis?.let { millis ->
                                        val date = GMTDate(millis)
                                        "${date.year}-${date.month.ordinal + 1}-${date.dayOfMonth}"
                                    } ?: ""
                                )
                                receiptViewModel.onDismissStartDate()
                            }
                            )
                        }
                        ) {
                            DatePicker(
                                state1, colors = DatePickerDefaults.colors(
                                    selectedDayContainerColor = Theme.colors.primary,
                                    todayDateBorderColor = Theme.colors.primary
                                )
                            )
                        }
                    }
                    EISimpleTextField(
                        modifier = Modifier.widthIn(max = 440.kms),
                        hint = "",
                        onValueChange = {

                        },
                        text = state.startDate,
                        keyboardType = KeyboardType.Text,
                        onTrailingIconClick = receiptViewModel::onShowStartDate,
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

                    val state2 = rememberDatePickerState()
                    AnimatedVisibility(state.dateToIsVisible) {
                        DatePickerDialog(onDismissRequest = receiptViewModel::onDismissEndDate, {
                            EIButton("Done", onClick = {
                                receiptViewModel.onChooseEndDateInputChange(
                                    state2.selectedDateMillis?.let { millis ->
                                        val date = GMTDate(millis)
                                        "${date.year}-${date.month.ordinal + 1}-${date.dayOfMonth}"
                                    } ?: ""
                                )
                                receiptViewModel.onDismissEndDate()
                            })
                        }) {
                            DatePicker(
                                state2, colors = DatePickerDefaults.colors(
                                    selectedDayContainerColor = Theme.colors.primary,
                                    todayDateBorderColor = Theme.colors.primary
                                )
                            )
                        }
                    }
                    EISimpleTextField(
                        modifier = Modifier.widthIn(max = 440.kms),
                        hint = "",
                        onValueChange = {

                        },
                        text = state.endDate,
                        keyboardType = KeyboardType.Text,
                        onTrailingIconClick = receiptViewModel::onShowEndDate,
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
                        onClick = { receiptViewModel.onClickSearch() },
                        textPadding = PaddingValues(horizontal = 24.kms),
                        modifier = Modifier.cursorHoverIconHand()
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    EIOutlinedButton(
                        title = Resources.strings.selectAll,
                        onClick = receiptViewModel::onClickSelectAll,
                        textPadding = PaddingValues(horizontal = 24.kms),
                        modifier = Modifier.cursorHoverIconHand()
                    )
                    EIButton(
                        title = Resources.strings.send,
                        onClick = receiptViewModel::onClickSend,
                        textPadding = PaddingValues(horizontal = 24.kms),
                        modifier = Modifier.cursorHoverIconHand(),
                        isLoading = state.isLoadingSend
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
                        TaxiRow(
                            receipt = receipt,
                            position = state.pageInfo.data.indexOf(receipt) + 1,
                            onEditTaxiClicked = {
                                receiptViewModel.onEditTaxiClicked(
                                    receipt.header.receiptNumber.toLongOrNull() ?: 0L
                                )
                            }
                        )
                    },
                )
                ReceiptsTableFooter(
                    selectedPage = state.currentPage,
                    numberItemInPage = state.specifiedReceipts,
                    numberOfReceipts = state.pageInfo.numberOfReceipts,
                    pageCount = state.pageInfo.totalPages,
                    onPageClicked = receiptViewModel::onPageClick,
                    onItemPerPageChanged = receiptViewModel::onItemsIndicatorChange,
                )
            }
            AnimatedVisibility(
                visible = state.isSnackBarSuccessVisible,
                enter = fadeIn(initialAlpha = 0.3f),
                exit = fadeOut(targetAlpha = 0.3f)
            ) {
                SnackBar(
                    modifier = Modifier.zIndex(3f).align(Alignment.BottomCenter),
                    onDismiss = receiptViewModel::onSnackBarSuccessDismiss
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
                    onDismiss = receiptViewModel::onSnackBarDismiss
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
fun ReceiptsTableFooter(
    numberItemInPage: Int,
    numberOfReceipts: Int,
    pageCount: Int,
    selectedPage: Int,
    onPageClicked: (Int) -> Unit,
    onItemPerPageChanged: (Int) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth().background(color = Theme.colors.surface),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TotalItemsIndicator(
            numberItemInPage = numberItemInPage,
            totalItems = numberOfReceipts,
            itemType = Resources.strings.receipts,
            onItemPerPageChange = onItemPerPageChanged
        )
        EIPager(
            maxPages = pageCount,
            currentPage = selectedPage,
            onPageClicked = onPageClicked,
        )
    }
}

@Composable
private fun RowScope.TaxiRow(
    position: Int,
    receipt: ReceiptDetailsUiState,
    onEditTaxiClicked: (Long) -> Unit,
) {
    TitleField(
        text = position.toString(),
        color = Theme.colors.contentTertiary,
        weight = 0.5f
    )
    //TitleField(text = receipt.id, weight = 2f)
    TitleField(text = receipt.header.receiptNumber, weight = 1f)
    TitleField(text = receipt.totalAmount.toString(), weight = 1f)
    //TitleField(text = "2500", weight = 3f)
    TitleField(text = receipt.taxTotals.sumOf { it.amount }.toString(), weight = 1f)
//    TitleField(text = "EGP", weight = 1.5f)
    TitleField(text = receipt.header.dateTimeIssued, weight = 1f)
    TitleField(text = receipt.header.uuid ?: "", weight = 2f)
    //TitleField(text = receipt.storeName, weight = 2f)
//    FlowRow(
//        modifier = Modifier.weight(3f),
//        horizontalArrangement = Arrangement.spacedBy(8.kms),
//        maxItemsInEachRow = 3,
//    ) {
//        repeat(4) {
//            Box(modifier = Modifier.padding(top = 8.kms)) {
//                Icon(
//                    painter = painterResource(Res.drawable.ic_support),
//                    contentDescription = null,
//                    tint = Theme.colors.contentPrimary.copy(alpha = 0.87f),
//                    modifier = Modifier.size(24.kms)
//                )
//            }
//        }
//    }
    Box(modifier = Modifier.weight(0.5f)) {
        EICheckBox(
            "",
            onCheck = {
                onEditTaxiClicked(receipt.header.receiptNumber.toLongOrNull() ?: 0L)
                receipt.isChecked.not()
            },
            isChecked = receipt.isChecked
        )
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
}


@Composable
fun RowScope.TitleField(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Theme.colors.contentPrimary,
    weight: Float = 3f
) {
    Text(
        text = text,
        style = Theme.typography.titleMedium,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier.weight(weight),
        maxLines = 1,
        color = color,
        textAlign = TextAlign.Justify
    )
}