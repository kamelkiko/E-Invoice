package data.remote.model.receipt_response

import kotlinx.serialization.Serializable

@Serializable
data class RejectedDocumentDTO(
    val receiptNumber: String? = null,
    val uuid: String? = null,
    val error: RejectedDocumentErrorDTO
)