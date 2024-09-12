package data.remote.model.details

import kotlinx.serialization.Serializable

@Serializable
data class AddressDetails(
    val country: String? = null,
    val governate: String? = null,
    val regionCity: String? = null,
    val street: String? = null,
    val buildingNumber: String? = null,
    val postalCode: String? = null,
    val floor: String? = null,
    val room: String? = null,
    val landmark: String? = null,
    val additionalInformation: String? = null,
    val countryName: String? = null,
    val countryNameAr: String? = null
)