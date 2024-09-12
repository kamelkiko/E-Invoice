package data.remote.model.details

import kotlinx.serialization.Serializable

@Serializable
data class ItemDataDetails(
    val internalCode: String? = null,
    val description: String? = null,
    val itemType: String? = null,
    val itemCode: String? = null,
    val unitType: String? = null,
    val quantity: Double? = null,
    val unitPrice: Double? = null,
    val netSale: Double? = null,
    val totalSale: Double? = null,
    val total: Double? = null,
    val itemCodeName: String? = null,
    val itemCodeNameAr: String? = null,
    val unitTypeName: String? = null,
    val commercialDiscount: List<DiscountDetails>,
    val itemDiscount: List<DiscountDetails>,
    val taxableItems: List<TaxableItemDetails>
)