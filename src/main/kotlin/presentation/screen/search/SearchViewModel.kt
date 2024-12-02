package presentation.screen.search

import cafe.adriel.voyager.core.model.screenModelScope
import data.util.AppConstants
import data.util.HistoryData
import domain.entity.DataWrapper
import domain.usecase.GetReceiptDetailsUseCase
import kotlinx.coroutines.*
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.ss.usermodel.WorkbookFactory
import presentation.base.BaseViewModel
import presentation.base.ErrorState
import java.awt.Desktop
import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import java.math.BigDecimal
import kotlin.math.ceil

class SearchViewModel(
    private val getReceiptDetails: GetReceiptDetailsUseCase,
) : BaseViewModel<SearchUiState, SearchUiEffect>(SearchUiState()), SearchInteractionListener {

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
        updateState { it.copy(isLoading = false, isLoadingImport = false) }
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
        updateState { it.copy(currentPage = pageNumber) }
        getReceipts()
    }

    override fun onClickImport() {
        updateState { it.copy(isLoadingImport = true) }
        val uuids = mutableListOf<String>()
        val file = chooseExcelFile()
        file?.let {
            viewModelScope.launch(Dispatchers.Default) {
                val workbook = WorkbookFactory.create(file)
                val sheet = workbook.getSheetAt(0)
                val headerRow = sheet.getRow(0)
                var uuidColumnIndex = -1
                for (cell in headerRow) {
                    if (cell.stringCellValue == "UUID") {
                        uuidColumnIndex = cell.columnIndex
                        break
                    }
                }
                if (uuidColumnIndex == -1) {
                    updateState { it.copy(isSnackBarVisible = true, snackBarTitle = "UUID column not found!") }
                }
                for (row in sheet) {
                    if (row.rowNum == 0) continue
                    val cell = row.getCell(uuidColumnIndex)
                    if (cell != null && cell.cellType == org.apache.poi.ss.usermodel.CellType.STRING) {
                        uuids.add(cell.stringCellValue)
                    }
                }
                workbook.close()
                updateState { it.copy(isLoadingImport = false, uuids = uuids) }
            }
        }
        updateState { it.copy(isLoadingImport = false) }
    }

    private fun chooseExcelFile(): File? {
        val frame = Frame()
        val dialog = FileDialog(frame, "Select Excel File", FileDialog.LOAD)
        dialog.file = "*.xlsx;*.xls" // Filter for Excel files
        dialog.isVisible = true
        val selectedFile = dialog.file?.let { "${dialog.directory}$it" }
        frame.dispose()
        return selectedFile?.let { File(it) }
    }

    override fun onClickExportAll() {
        updateState { it.copy(isLoadingExportAll = true) }
//        viewModelScope.launch(Dispatchers.Default) {
//            workbook {
//                sheet("Receipts") {
//                    receiptsHeader()
//                    state.value.pageInfo.data.forEachIndexed { no, receipt ->
//                        row {
//                            cell(no + 1)
//                            cell(receipt.receiptNumber)
//                            cell(receipt.dateTimeIssued)
//                            cell(receipt.totalSales.toString())
//                            cell(receipt.totalCommercialDiscount.toString())
//                            cell(receipt.feesAmount.toString())
//                            cell(receipt.netAmount.toString())
//                            cell(receipt.taxTotals.toString())
//                            cell(receipt.totalAmount.toString())
//                            cell(receipt.status)
//                            cell(receipt.storeName)
//                        }
//                    }
//                }
//            }.write("receipts.xlsx")
//
//            val excelFile = File("receipts.xlsx")
//            if (Desktop.isDesktopSupported()) {
//                withContext(Dispatchers.IO) {
//                    Desktop.getDesktop().open(excelFile)
//                }
//            }
//
//            updateState {
//                it.copy(
//                    isLoadingImport = false,
//                    isSnackBarSuccessVisible = true,
//                    snackBarSuccessTitle = "Exported excel done!"
//                )
//            }
//        }
    }

    override fun onClickSearch() {

    }

    override fun onSearchInputChange(q: String) {
        updateState { it.copy(uuid = q) }
    }

//    private fun Sheet.receiptsHeader() {
//        val headings = listOf(
//            "No.",
//            "Receipt Number",
//            "DateTime Issued",
//            "Total Sales",
//            "Total Commercial Discount",
//            "Fees Amount",
//            "Net Amount",
//            "Tax Totals",
//            "Total Amount",
//            "Status",
//            "Store Name"
//        )
//
//        val headingStyle = createCellStyle {
//            setFont(
//                createFont {
//                    color = IndexedColors.WHITE.index
//                }
//            )
//
//            fillPattern = FillPatternType.SOLID_FOREGROUND
//            fillForegroundColor = IndexedColors.DARK_GREEN.index
//        }
//
//        row(headingStyle) {
//            headings.forEach { cell(it) }
//        }
//    }

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