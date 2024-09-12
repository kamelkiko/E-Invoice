package presentation.screen.receipts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import domain.entity.*
import kotlinx.datetime.*
import presentation.screen.composable.table.Header
import resource.Resources
import java.math.BigDecimal

@Immutable
data class ReceiptUiState(
    val isLoading: Boolean = false,
    val isLoadingSend: Boolean = false,
    val hasConnection: Boolean = true,
    val receipts: List<ReceiptDetailsUiState> = emptyList(),
    val selectedReceipts: List<ReceiptDetailsUiState> = emptyList(),
    val pageInfo: ReceiptPageInfoUiState = ReceiptPageInfoUiState(),
    val specifiedReceipts: Int = 30,
    val currentPage: Int = 1,
    val unSelect: Boolean = false,
    val dateFromIsVisible: Boolean = false,
    val dateToIsVisible: Boolean = false,
    val startDate: String = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date.toString(),
    val endDate: String = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date.toString(),
    val snackBarTitle: String? = null,
    val isSnackBarVisible: Boolean = false,
    val isSnackBarSuccessVisible: Boolean = false,
    val snackBarSuccessTitle: String? = null,
) {
    val tabHeader
        @Composable get() = listOf(
            Header(Resources.strings.number, 0.5f),
            //Header(Resources.strings.invoiceId, 2f),
            Header(Resources.strings.invoiceNumber, 1f),
            Header(Resources.strings.totalValues, 1f),
            // Header(Resources.strings.subTotal, 3f),
            Header(Resources.strings.totalTaxes, 1f),
//            Header(Resources.strings.currency, 1.5f),
            Header(Resources.strings.date, 1f),
            Header(Resources.strings.uuid, 2f),
            // Header(Resources.strings.storeName, 2f),
            Header("", 0.5f),
        )
}

@Immutable
data class ReceiptPageInfoUiState(
    val data: List<ReceiptDetailsUiState> = emptyList(),
    val numberOfReceipts: Int = 0,
    val totalPages: Int = 0,
)

@Immutable
data class ReceiptDetailsUiState(
    val header: HeaderUiState = HeaderUiState(),
    val documentType: DocumentTypeUiState = DocumentTypeUiState(),
    val seller: SellerUiState = SellerUiState(),
    val buyer: BuyerUiState = BuyerUiState(),
    val itemData: List<ItemDataUiState> = emptyList(),
    val totalSales: BigDecimal = BigDecimal(0.0),
    val totalCommercialDiscount: BigDecimal = BigDecimal(0.0),
    val netAmount: BigDecimal = BigDecimal(0.0),
    val feesAmount: Double = 0.0,
    val totalAmount: BigDecimal = BigDecimal(0.0),
    val taxTotals: List<TaxTotalUiState> = emptyList(),
    val paymentMethod: String = "",
    val isChecked: Boolean = false,
) {
    fun toEntity(): Receipt = Receipt(
        header = header.toEntity(),
        documentType = documentType.toEntity(),
        seller = seller.toEntity(),
        buyer = buyer.toEntity(),
        itemData = itemData.map { it.toEntity() },
        totalSales = totalSales,
        totalCommercialDiscount = totalCommercialDiscount,
        netAmount = netAmount,
        feesAmount = feesAmount,
        totalAmount = totalAmount,
        taxTotals = taxTotals.map { it.toEntity() },
        paymentMethod = paymentMethod
    )
}

@Immutable
data class HeaderUiState(
    val dateTimeIssued: String = "",
    val receiptNumber: String = "",
    val uuid: String = "",
    val previousUUID: String = "",
    val referenceOldUUID: String = "",
    val currency: String = "",
    val exchangeRate: Int = 0,
    val sOrderNameCode: String = "",
    //  val orderDeliveryMode: String = "",
    val grossWeight: Int = 0,
    val netWeight: Int = 0
) {
    fun toEntity(): domain.entity.Header {
        val originalDate = dateTimeIssued
        val parsedDateTime = LocalDateTime.parse(originalDate.replace(" ", "T").removeSuffix(".0"))
        val instant = parsedDateTime.toInstant(TimeZone.UTC)
        return Header(
            dateTimeIssued = "2024-09-10T23:42:00Z",
            receiptNumber = receiptNumber,
            uuid = uuid,
            previousUUID = previousUUID,
            referenceOldUUID = referenceOldUUID,
            currency = currency,
            exchangeRate = exchangeRate,
            sOrderNameCode = sOrderNameCode,
            // orderDeliveryMode = orderDeliveryMode,
            grossWeight = grossWeight,
            netWeight = netWeight
        )
    }
}

fun domain.entity.Header.toUiState(): HeaderUiState {
    return HeaderUiState(
        dateTimeIssued = dateTimeIssued,
        receiptNumber = receiptNumber,
        uuid = uuid,
        previousUUID = previousUUID,
        referenceOldUUID = referenceOldUUID,
        currency = currency,
        exchangeRate = exchangeRate,
        sOrderNameCode = sOrderNameCode,
        //orderDeliveryMode = orderDeliveryMode,
        grossWeight = grossWeight,
        netWeight = netWeight
    )
}

@Immutable
data class DocumentTypeUiState(
    val receiptType: String = "",
    val typeVersion: String = ""
) {
    fun toEntity(): DocumentType = DocumentType(
        receiptType = receiptType,
        typeVersion = typeVersion
    )
}

fun DocumentType.toUiState(): DocumentTypeUiState {
    return DocumentTypeUiState(
        receiptType = receiptType,
        typeVersion = typeVersion
    )
}

@Immutable
data class BranchAddressUiState(
    val country: String = "",
    val governate: String = "",
    val regionCity: String = "",
    val street: String = "",
    val buildingNumber: String = "",
    val postalCode: String = "",
    val floor: String = "",
    val room: String = "",
    val landmark: String = "",
    val additionalInformation: String = ""
) {
    fun toEntity(): BranchAddress = BranchAddress(
        country = country,
        governate = governate,
        regionCity = regionCity,
        street = street,
        buildingNumber = buildingNumber,
        postalCode = postalCode,
        floor = floor,
        room = room,
        landmark = landmark,
        additionalInformation = additionalInformation
    )
}

fun BranchAddress.toUiState(): BranchAddressUiState {
    return BranchAddressUiState(
        country = country,
        governate = governate,
        regionCity = regionCity,
        street = street,
        buildingNumber = buildingNumber,
        postalCode = postalCode,
        floor = floor,
        room = room,
        landmark = landmark,
        additionalInformation = additionalInformation
    )
}

@Immutable
data class SellerUiState(
    val rin: String = "",
    val companyTradeName: String = "",
    val branchCode: String = "",
    val branchAddress: BranchAddressUiState = BranchAddressUiState(),
    val deviceSerialNumber: String = "",
    val syndicateLicenseNumber: String = "",
    val activityCode: String = ""
) {
    fun toEntity(): Seller = Seller(
        rin = rin,
        companyTradeName = companyTradeName,
        branchCode = branchCode,
        branchAddress = branchAddress.toEntity(),
        deviceSerialNumber = deviceSerialNumber,
        syndicateLicenseNumber = syndicateLicenseNumber,
        activityCode = activityCode
    )
}

fun Seller.toUiState(): SellerUiState {
    return SellerUiState(
        rin = rin,
        companyTradeName = companyTradeName,
        branchCode = branchCode,
        branchAddress = branchAddress.toUiState(),
        deviceSerialNumber = deviceSerialNumber,
        syndicateLicenseNumber = syndicateLicenseNumber,
        activityCode = activityCode
    )
}

@Immutable
data class BuyerUiState(
    val type: String = "",
    val id: String = "",
    val name: String = "",
    val mobileNumber: String = ""
) {
    fun toEntity(): Buyer = Buyer(
        type = type,
        id = id,
        name = name,
        mobileNumber = mobileNumber
    )
}

fun Buyer.toUiState(): BuyerUiState {
    return BuyerUiState(
        type = type,
        id = id,
        name = name,
        mobileNumber = mobileNumber
    )
}

@Immutable
data class ItemDataUiState(
    val internalCode: String = "",
    val description: String = "",
    val itemType: String = "",
    val itemCode: String = "",
    val unitType: String = "",
    val quantity: BigDecimal = BigDecimal(0.0),
    val unitPrice: BigDecimal = BigDecimal(0.0),
    val netSale: BigDecimal = BigDecimal(0.0),
    val totalSale: BigDecimal = BigDecimal(0.0),
    val total: BigDecimal = BigDecimal(0.0),
    val commercialDiscountData: List<CommercialDiscountDataUiState> = emptyList(),
    val itemDiscountData: List<ItemDiscountDataUiState> = emptyList(),
    val taxableItems: List<TaxableItemUiState> = emptyList()
) {
    fun toEntity(): ItemData = ItemData(
        internalCode = internalCode,
        description = description,
        itemType = itemType,
        itemCode = itemCode,
        unitType = unitType,
        quantity = quantity,
        unitPrice = unitPrice,
        netSale = netSale,
        totalSale = totalSale,
        total = total,
        commercialDiscountData = commercialDiscountData.map { it.toEntity() },
        itemDiscountData = itemDiscountData.map { it.toEntity() },
        taxableItems = taxableItems.map { it.toEntity() }
    )
}

fun ItemData.toUiState(): ItemDataUiState {
    return ItemDataUiState(
        internalCode = internalCode,
        description = description,
        itemType = itemType,
        itemCode = itemCode,
        unitType = unitType,
        quantity = quantity,
        unitPrice = unitPrice,
        netSale = netSale,
        totalSale = totalSale,
        total = total,
        commercialDiscountData = commercialDiscountData.map { it.toUiState() },
        itemDiscountData = itemDiscountData.map { it.toUiState() },
        taxableItems = taxableItems.map { it.toUiState() }
    )
}

@Immutable
data class CommercialDiscountDataUiState(
    val amount: BigDecimal = BigDecimal(0.0),
    val description: String = "",
    val rate: Double = 0.0
) {
    fun toEntity(): CommercialDiscountData = CommercialDiscountData(
        amount = amount,
        description = description,
        rate = rate
    )
}

fun CommercialDiscountData.toUiState(): CommercialDiscountDataUiState {
    return CommercialDiscountDataUiState(
        amount = amount,
        description = description,
        rate = rate
    )
}

@Immutable
data class ItemDiscountDataUiState(
    val amount: BigDecimal = BigDecimal(0.0),
    val description: String = "",
    val rate: Double = 0.0
) {
    fun toEntity(): ItemDiscountData = ItemDiscountData(
        amount = amount,
        description = description,
        rate = rate
    )
}

fun ItemDiscountData.toUiState(): ItemDiscountDataUiState {
    return ItemDiscountDataUiState(
        amount = amount,
        description = description,
        rate = rate
    )
}

@Immutable
data class TaxableItemUiState(
    val taxType: String = "",
    val amount: BigDecimal = BigDecimal(0.0),
    val subType: String = "",
    val rate: Double = 0.0
) {
    fun toEntity(): TaxableItem = TaxableItem(
        taxType = taxType,
        amount = amount,
        subType = subType,
        rate = rate
    )
}

fun TaxableItem.toUiState(): TaxableItemUiState {
    return TaxableItemUiState(
        taxType = taxType,
        amount = amount,
        subType = subType,
        rate = rate
    )
}

@Immutable
data class TaxTotalUiState(
    val taxType: String = "",
    val amount: BigDecimal = BigDecimal(0.0)
) {
    fun toEntity(): TaxTotal = TaxTotal(
        taxType = taxType,
        amount = amount
    )
}

fun TaxTotal.toUiState(): TaxTotalUiState {
    return TaxTotalUiState(
        taxType = taxType,
        amount = amount
    )
}

fun DataWrapper<Receipt>.toDetailsUiState(): ReceiptPageInfoUiState {
    return ReceiptPageInfoUiState(
        data = result.toDetailsUiState(),
        totalPages = totalPages,
        numberOfReceipts = numberOfResult
    )
}

fun List<Receipt>.toDetailsUiState() = map { it.toDetailsUiState() }

fun Receipt.toDetailsUiState(): ReceiptDetailsUiState = ReceiptDetailsUiState(
    header = header.toUiState(),
    documentType = documentType.toUiState(),
    seller = seller.toUiState(),
    buyer = buyer.toUiState(),
    itemData = itemData.map { it.toUiState() },
    totalSales = totalSales,
    totalCommercialDiscount = totalCommercialDiscount,
    netAmount = netAmount,
    feesAmount = feesAmount,
    totalAmount = totalAmount,
    taxTotals = taxTotals.map { it.toUiState() },
    paymentMethod = paymentMethod
)