package data.remote.model.details

import kotlinx.serialization.Serializable

@Serializable
data class ReceiptDetails(
    val uuid: String? = null,
    val longId: String? = null,
    val previousUUID: String? = null,
    val referenceOldUUID: String? = null,
    val dateTimeIssued: String? = null,
    val dateTimeReceived: String? = null,
    val receiptNumber: String? = null,
    val currency: String? = null,
    val exchangeRate: Double? = null,
    val sOrderNameCode: String? = null,
    val grossWeight: Double? = null,
    val netWeight: Double? = null,
    val documentType: DocumentTypeDetails,
    val totalSales: Double? = null,
    val totalAmount: Double? = null,
    val totalCommercialDiscount: Double? = null,
    val netAmount: Double? = null,
    val feesAmount: Double? = null,
    val paymentMethod: String? = null,
    val status: String? = null,
    val statusReason: String? = null,
    val hasReturnReceipts: Boolean? = null,
    val seller: SellerDetails,
    val buyer: BuyerDetails,
    val itemData: List<ItemDataDetails>,
    val taxTotals: List<TaxTotalDetails>,
    val extraReceiptDiscount: List<DiscountDetails>,
    val returnReceipts: List<ReturnReceiptDetails>
)

@Serializable
data class ReturnReceiptDetails(
    val id: Int? = null
)