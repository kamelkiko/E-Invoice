package data.remote.model.details

import kotlinx.serialization.Serializable

@Serializable
data class SellerDetails(
    val sellerId: String? = null,
    val sellerName: String? = null,
    val branchCode: String? = null,
    val deviceSerialNumber: String? = null,
    val syndicateLicenseNumber: String? = null,
    val activityCode: String? = null,
    val branchAddress: AddressDetails
)