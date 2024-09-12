package presentation.screen.history

import cafe.adriel.voyager.core.model.screenModelScope
import data.util.AppConstants
import data.util.HistoryData
import domain.entity.DataWrapper
import domain.usecase.GetReceiptDetailsUseCase
import io.github.evanrupert.excelkt.Sheet
import io.github.evanrupert.excelkt.workbook
import kotlinx.coroutines.*
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.IndexedColors
import presentation.base.BaseViewModel
import presentation.base.ErrorState
import java.awt.Desktop
import java.io.File
import java.math.BigDecimal
import kotlin.math.ceil

class HistoryViewModel(
    private val getReceiptDetails: GetReceiptDetailsUseCase,
) : BaseViewModel<HistoryUiState, HistoryUiEffect>(HistoryUiState()), HistoryInteractionListener {

    private var limitJob: Job? = null

    override val viewModelScope: CoroutineScope get() = screenModelScope

    init {
        getReceipts()
    }

    private fun getReceipts() {
        updateState { it.copy(isLoading = true, hasConnection = true) }
        var index = 0
        val data = viewModelScope.async(Dispatchers.Default) {
            AppConstants.historyData.filter { it.uuid.isNotEmpty() }.forEach { history ->
                try {
                    index = AppConstants.historyData.indexOf(history)
                    val data = getReceiptDetails(history.uuid)
                    if (data.message.isNullOrEmpty())
                        AppConstants.historyData[index] = HistoryData(
                            uuid = history.uuid,
                            submitUUID = history.submitUUID,
                            dateTimeIssued = data.dateTimeIssued ?: "",
                            dateTimeReceived = data.dateTimeReceived ?: "",
                            receiptNumber = data.receipt?.receiptNumber ?: "",
                            totalSales = data.receipt?.totalSales?.toBigDecimal() ?: BigDecimal(0.0),
                            totalCommercialDiscount = data.receipt?.totalCommercialDiscount?.toBigDecimal()
                                ?: BigDecimal(0.0),
                            feesAmount = data.receipt?.feesAmount ?: 0.0,
                            netAmount = data.receipt?.netAmount?.toBigDecimal() ?: BigDecimal(0.0),
                            taxTotals = data.receipt?.taxTotals?.sumOf { it.amount }?.toBigDecimal() ?: BigDecimal(0.0),
                            totalAmount = data.receipt?.totalAmount?.toBigDecimal() ?: BigDecimal(0.0),
                            status = data.receipt?.status ?: "Invalid",
                            storeName = AppConstants.storeName
                        )
                    else
                        AppConstants.historyData[index] = history.copy(
                            status = "Invalid"
                        )
                    AppConstants.historyData
                } catch (e: Exception) {
                    AppConstants.historyData[index] = history.copy(
                        status = "Invalid"
                    )
                }
            }
            AppConstants.historyData
        }
        tryToExecute(
            function = {
                state.value.run {
                    val result = data.await()
                    val startIndex = (currentPage - 1) * specifiedReceipts
                    val endIndex = startIndex + specifiedReceipts
                    val numberOfPages = if (result.isNotEmpty())
                        ceil(result.size / (specifiedReceipts * 1.0)).toInt() else 0
                    DataWrapper(
                        totalPages = numberOfPages,
                        numberOfResult = if (result.isNotEmpty()) result.size else 0,
                        result = if (result.isEmpty()) emptyList() else
                            result.subList(startIndex, endIndex.coerceAtMost(result.size))
                    )
                }
            },
            onSuccess = ::onGetReceiptSuccessfully,
            onError = ::onError
        )
    }

    private fun onGetReceiptSuccessfully(receipts: DataWrapper<HistoryData>) {
        updateState {
            it.copy(
                pageInfo = receipts.toDetailsUiState(),
                isLoading = false,
                hasConnection = true
            )
        }
        if (state.value.currentPage > state.value.pageInfo.totalPages && receipts.toDetailsUiState().data.isNotEmpty()) {
            onPageClick(state.value.pageInfo.totalPages)
        }
    }

    private fun onError(error: ErrorState) {
        updateState { it.copy(isLoading = false, isLoadingExportAll = false) }
        when (error) {
            is ErrorState.NetworkError -> {
                updateState {
                    it.copy(
                        hasConnection = false,
                        isSnackBarVisible = true,
                        snackBarTitle = error.message ?: "",
                    )
                }
            }

            is ErrorState.ValidationNetworkError -> {
                updateState {
                    it.copy(
                        isSnackBarVisible = true,
                        snackBarTitle = error.message ?: "",
                    )
                }
            }

            is ErrorState.UnAuthorized -> {
                updateState {
                    it.copy(
                        isSnackBarVisible = true,
                        snackBarTitle = error.message ?: "",
                    )
                }
            }

            is ErrorState.NotFound -> {
                updateState {
                    it.copy(
                        isSnackBarVisible = true,
                        snackBarTitle = error.message ?: "",
                    )
                }
            }

            is ErrorState.UnknownError -> {
                updateState {
                    it.copy(
                        isSnackBarVisible = true,
                        snackBarTitle = error.message ?: "",
                    )
                }
            }

            else -> updateState {
                it.copy(
                    isSnackBarVisible = true,
                    snackBarTitle = "Something happened please try again!",
                )
            }
        }

    }

    override fun onItemsIndicatorChange(itemPerPage: Int) {
        updateState { it.copy(specifiedReceipts = itemPerPage) }
        launchLimitJob()
    }

    private fun launchLimitJob() {
        limitJob?.cancel()
        limitJob = launchDelayed(300L) { getReceipts() }
    }

    override fun onPageClick(pageNumber: Int) {
        updateState { it.copy(currentPage = pageNumber, selectedReceipts = emptyList()) }
        getReceipts()
    }

//    override fun onEditTaxiClicked(itemId: Long) {
//        val currentTaxisState = state.value.pageInfo.data
//        val selectedItemState = currentTaxisState.first { (it.receiptNumber.toLongOrNull() ?: 0L) == itemId }
//        val updatedTaxiState = selectedItemState.copy(isChecked = selectedItemState.isChecked.not())
//        updateState {
//            it.copy(
//                pageInfo = state.value.pageInfo.copy(data = currentTaxisState.toMutableList()
//                    .apply { set(indexOf(selectedItemState), updatedTaxiState) }),
//                selectedReceipts = state.value.selectedReceipts.toMutableList().apply {
//                    if (updatedTaxiState.isChecked) {
//                        add(updatedTaxiState)
//                    } else {
//                        removeIf { r -> (r.receiptNumber.toLongOrNull() ?: 0L) == itemId }
//                    }
//                }
//            )
//        }
//    }

    override fun onClickExportAll() {
        updateState { it.copy(isLoadingExportAll = true) }
        viewModelScope.launch(Dispatchers.Default) {
            workbook {
                sheet("Receipts") {
                    receiptsHeader()
                    state.value.pageInfo.data.forEachIndexed { no, receipt ->
                        row {
                            cell(no + 1)
                            cell(receipt.receiptNumber)
                            cell(receipt.dateTimeIssued)
                            cell(receipt.totalSales.toString())
                            cell(receipt.totalCommercialDiscount.toString())
                            cell(receipt.feesAmount.toString())
                            cell(receipt.netAmount.toString())
                            cell(receipt.taxTotals.toString())
                            cell(receipt.totalAmount.toString())
                            cell(receipt.status)
                            cell(receipt.storeName)
                        }
                    }
                }
            }.write("receipts.xlsx")

            val excelFile = File("receipts.xlsx")
            if (Desktop.isDesktopSupported()) {
                withContext(Dispatchers.IO) {
                    Desktop.getDesktop().open(excelFile)
                }
            }

            updateState {
                it.copy(
                    isLoadingExportAll = false,
                    isSnackBarSuccessVisible = true,
                    snackBarSuccessTitle = "Exported excel done!"
                )
            }
        }
    }

    private fun Sheet.receiptsHeader() {
        val headings = listOf(
            "No.",
            "Receipt Number",
            "DateTime Issued",
            "Total Sales",
            "Total Commercial Discount",
            "Fees Amount",
            "Net Amount",
            "Tax Totals",
            "Total Amount",
            "Status",
            "Store Name"
        )

        val headingStyle = createCellStyle {
            setFont(
                createFont {
                    color = IndexedColors.WHITE.index
                }
            )

            fillPattern = FillPatternType.SOLID_FOREGROUND
            fillForegroundColor = IndexedColors.DARK_GREEN.index
        }

        row(headingStyle) {
            headings.forEach { cell(it) }
        }
    }

    override fun onRetry() {
        getReceipts()
    }

    override fun onSnackBarDismiss() {
        updateState {
            it.copy(
                isSnackBarVisible = false
            )
        }
    }

    override fun onSnackBarSuccessDismiss() {
        updateState {
            it.copy(
                isSnackBarSuccessVisible = false
            )
        }
    }
}