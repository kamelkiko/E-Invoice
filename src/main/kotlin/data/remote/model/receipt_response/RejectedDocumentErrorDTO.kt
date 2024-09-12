package data.remote.model.receipt_response

import kotlinx.serialization.Serializable

@Serializable
data class RejectedDocumentErrorDTO(
    val message: String? = null,
    val target: String? = null,
    val details: List<RejectedDocumentDetailsDto>,
)

@Serializable
data class RejectedDocumentDetailsDto(
    val code: String? = null,
    val message: String? = null,
    val target: String? = null,
    val propertyPath: String? = null,
)