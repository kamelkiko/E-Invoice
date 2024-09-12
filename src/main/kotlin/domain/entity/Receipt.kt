package domain.entity

import kotlinx.serialization.Serializable
import util.BigDecimalSerializer
import java.math.BigDecimal

@Serializable
data class Receipt(
    val header: Header,
    val documentType: DocumentType,
    val seller: Seller,
    val buyer: Buyer,
    val itemData: List<ItemData>,
    @Serializable(with = BigDecimalSerializer::class)
    val totalSales: BigDecimal,
    @Serializable(with = BigDecimalSerializer::class)
    val totalCommercialDiscount: BigDecimal,
    @Serializable(with = BigDecimalSerializer::class)
    val netAmount: BigDecimal,
    val feesAmount: Double,
    @Serializable(with = BigDecimalSerializer::class)
    val totalAmount: BigDecimal,
    val taxTotals: List<TaxTotal>,
    val paymentMethod: String,
)

@Serializable
data class Header(
    val dateTimeIssued: String,
    val receiptNumber: String,
    val uuid: String,
    val previousUUID: String,
    val referenceOldUUID: String,
    val currency: String,
    val exchangeRate: Int,
    val sOrderNameCode: String,
    // val orderDeliveryMode: String,
    val grossWeight: Int,
    val netWeight: Int
)

@Serializable
data class DocumentType(
    val receiptType: String,
    val typeVersion: String
)

@Serializable
data class Seller(
    val rin: String,
    val companyTradeName: String,
    val branchCode: String,
    val branchAddress: BranchAddress,
    val deviceSerialNumber: String,
    val syndicateLicenseNumber: String,
    val activityCode: String
)

@Serializable
data class BranchAddress(
    val country: String,
    val governate: String,
    val regionCity: String,
    val street: String,
    val buildingNumber: String,
    val postalCode: String,
    val floor: String,
    val room: String,
    val landmark: String,
    val additionalInformation: String
)

@Serializable
data class Buyer(
    val type: String,
    val id: String,
    val name: String,
    val mobileNumber: String
)

@Serializable
data class ItemData(
    val internalCode: String,
    val description: String,
    val itemType: String,
    val itemCode: String,
    val unitType: String,
    @Serializable(with = BigDecimalSerializer::class)
    val quantity: BigDecimal,
    @Serializable(with = BigDecimalSerializer::class)
    val unitPrice: BigDecimal,
    @Serializable(with = BigDecimalSerializer::class)
    val netSale: BigDecimal,
    @Serializable(with = BigDecimalSerializer::class)
    val totalSale: BigDecimal,
    @Serializable(with = BigDecimalSerializer::class)
    val total: BigDecimal,
    val commercialDiscountData: List<CommercialDiscountData>,
    val itemDiscountData: List<ItemDiscountData>,
    val taxableItems: List<TaxableItem>
)

@Serializable
data class CommercialDiscountData(
    @Serializable(with = BigDecimalSerializer::class)
    val amount: BigDecimal,
    val description: String,
    val rate: Double
)

@Serializable
data class ItemDiscountData(
    @Serializable(with = BigDecimalSerializer::class)
    val amount: BigDecimal,
    val description: String,
    val rate: Double
)

@Serializable
data class TaxableItem(
    val taxType: String,
    @Serializable(with = BigDecimalSerializer::class)
    val amount: BigDecimal,
    val subType: String,
    val rate: Double
)

@Serializable
data class TaxTotal(
    val taxType: String,
    @Serializable(with = BigDecimalSerializer::class)
    val amount: BigDecimal
)