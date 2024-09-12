package data.remote.model.details

import kotlinx.serialization.Serializable

@Serializable
data class DocumentDetailsData(
    val rawDocument: String? = null,
    val submissionUuid: String? = null,
    val dateTimeIssued: String? = null,
    val dateTimeReceived: String? = null,
    val submissionChannel: String? = null,
    val maxPrecision: Int? = null,
    val receipt: ReceiptDetails? = null,
    val correlationId: String? = null,
    val target: String? = null,
    val code: String? = null,
    val message: String? = null,
)