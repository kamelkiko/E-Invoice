package data.remote.model.receipt_response

import kotlinx.serialization.Serializable

@Serializable
data class DocumentDTO(
    val uuid: String? = null,
    val longId: String? = null,
    val receiptNumber: String? = null,
    val hashKey: String? = null,
)