package data.remote.model.details

import kotlinx.serialization.Serializable

@Serializable
data class DiscountDetails(
    val amount: Double?=null,
    val description: String?=null,
    val rate: Double?=null
)
