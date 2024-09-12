package data.remote.model.receipt_response

import kotlinx.serialization.Serializable

@Serializable
data class HeaderDTO(
    val statusCode: String,
    val code: String,
    val details: List<String>,
    val correlationId: String,
    val requestTime: String,
    val responseProcessingTimeInTicks: Long
)