package data.local.model

import kotlinx.serialization.Serializable

@Serializable
data class StoreAuth(
    val clientId: String? = null,
    val clientSecret: String? = null,
    val posSerial: String? = null,
    val posOsVersion: String? = null,
)