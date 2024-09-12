package data.remote.model.details

import kotlinx.serialization.Serializable

@Serializable
data class TaxableItemDetails(
    val taxType: String? = null,
    val subType: String? = null,
    val amount: Double? = null,
    val rate: Double? = null,
    val taxTypeName: String? = null,
    val taxTypeNameAr: String? = null,
    val sign: Int? = null,
    val exchangeRate: Double? = null
)