package presentation.screen.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import data.remote.model.details.DocumentDetailsData
import data.util.AppConstants
import data.util.HistoryData
import domain.entity.DataWrapper
import presentation.screen.composable.table.Header
import resource.Resources
import java.math.BigDecimal

@Immutable
data class SearchUiState(
    val isLoading: Boolean = false,
    val isLoadingImport: Boolean = false,
    val isLoadingExportAll: Boolean = false,
    val hasConnection: Boolean = true,
    val receipts: List<SearchDetailsUiState> = emptyList(),
    val pageInfo: ReceiptPageInfoUiState = ReceiptPageInfoUiState(),
    val specifiedReceipts: Int = 30,
    val currentPage: Int = 1,
    val snackBarTitle: String? = null,
    val isSnackBarVisible: Boolean = false,
    val isSnackBarSuccessVisible: Boolean = false,
    val snackBarSuccessTitle: String? = null,
    val uuid: String = "",
    val uuids: List<String> = emptyList(),
) {
    val tabHeader
        @Composable get() = listOf(
            Header(Resources.strings.number, 0.5f),
            //Header(Resources.strings.invoiceId, 2f),
            Header(Resources.strings.invoiceNumber, 1f),
//            Header(Resources.strings.totalValues, 1f),
            // Header(Resources.strings.subTotal, 3f),
//            Header(Resources.strings.totalTaxes, 1f),
//            Header(Resources.strings.currency, 1.5f),
            Header(Resources.strings.date, 2f),
            Header(Resources.strings.uuid, 2f),
            Header("Submit UUID", 2f),
            // Header(Resources.strings.storeName, 2f),
            Header("Status", 0.5f),
        )
}

@Immutable
data class ReceiptPageInfoUiState(
    val data: List<SearchDetailsUiState> = emptyList(),
    val numberOfReceipts: Int = 0,
    val totalPages: Int = 0,
)

@Immutable
data class SearchDetailsUiState(
    val uuid: String = "",
    val submitUUID: String = "",
    val dateTimeIssued: String = "",
    val dateTimeReceived: String = "",
    val receiptNumber: String = "",
    val totalSales: BigDecimal = BigDecimal(0.0),
    val totalCommercialDiscount: BigDecimal = BigDecimal(0.0),
    val netAmount: BigDecimal = BigDecimal(0.0),
    val feesAmount: Double = 0.0,
    val totalAmount: BigDecimal = BigDecimal(0.0),
    val taxTotals: BigDecimal = BigDecimal(0.0),
    val storeName: String = "",
    val status: String = "Unknown",
)

fun DataWrapper<HistoryData>.toDetailsUiState(): ReceiptPageInfoUiState {
    return ReceiptPageInfoUiState(
        data = result.toDetailsUiState(),
        totalPages = totalPages,
        numberOfReceipts = numberOfResult
    )
}

fun List<HistoryData>.toDetailsUiState() = map { it.toSearchDetailsUiState() }

fun DocumentDetailsData.toDetailsUiState(): SearchDetailsUiState {
    return SearchDetailsUiState(
        uuid = receipt?.uuid ?: "",
        submitUUID = submissionUuid ?: "",
        dateTimeIssued = dateTimeIssued?.replace("T", " ")?.replace("Z", "") ?: "",
        dateTimeReceived = dateTimeReceived ?: "",
        receiptNumber = receipt?.receiptNumber ?: "",
        totalSales = receipt?.totalSales?.toBigDecimal() ?: BigDecimal(0.0),
        taxTotals = receipt?.taxTotals?.sumOf { it.amount }?.toBigDecimal() ?: BigDecimal(0.0),
        totalAmount = receipt?.totalAmount?.toBigDecimal() ?: BigDecimal(0.0),
        feesAmount = receipt?.feesAmount ?: 0.0,
        netAmount = receipt?.netAmount?.toBigDecimal() ?: BigDecimal(0.0),
        totalCommercialDiscount = receipt?.totalCommercialDiscount?.toBigDecimal() ?: BigDecimal(0.0),
        storeName = AppConstants.storeName,
        status = receipt?.status ?: "Invalid",
    )
}