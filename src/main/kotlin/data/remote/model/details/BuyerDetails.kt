package data.remote.model.details

import kotlinx.serialization.Serializable

@Serializable
data class BuyerDetails(
    val buyerId: String? = null,
    val buyerName: String? = null,
    val type: String? = null,
    val mobileNumber: String? = null
)