package data.util

import kotlinx.coroutines.flow.MutableStateFlow
import presentation.screen.history.HistoryDetailsUiState
import util.LanguageCode
import java.math.BigDecimal

object AppLanguage {
    val code: MutableStateFlow<String> = MutableStateFlow(LanguageCode.EN.value)
}

object AppConstants {
    var storeId: Int = 2
    var storeName: String = "TERRENAL"
    var isAdmin = false
    var historyData = mutableListOf<HistoryData>()
}

data class HistoryData(
    val uuid: String = "56c8392adbe378c612a464b30f1298e1d48f258d580883b8bce0e29f89c2e0c5",
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
) {
    fun toHistoryDetailsUiState(): HistoryDetailsUiState = HistoryDetailsUiState(
        uuid = uuid,
        submitUUID = submitUUID,
        dateTimeIssued = dateTimeIssued,
        dateTimeReceived = dateTimeReceived,
        receiptNumber = receiptNumber,
        totalSales = totalSales,
        totalCommercialDiscount = totalCommercialDiscount,
        netAmount = netAmount,
        feesAmount = feesAmount,
        totalAmount = totalAmount,
        taxTotals = taxTotals,
        storeName = storeName,
        status = status
    )
}