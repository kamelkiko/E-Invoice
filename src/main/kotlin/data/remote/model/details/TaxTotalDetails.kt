package data.remote.model.details

import kotlinx.serialization.Serializable

@Serializable
data class TaxTotalDetails(
    val taxType: String? = null,
    val amount: Double,
    val taxTypeName: String? = null,
    val taxTypeNameAr: String? = null,
    val exchangeRate: Double? = null,
)