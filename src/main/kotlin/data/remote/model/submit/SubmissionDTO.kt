package data.remote.model.submit

import data.remote.model.receipt_response.DocumentDTO
import data.remote.model.receipt_response.HeaderDTO
import data.remote.model.receipt_response.RejectedDocumentDTO
import kotlinx.serialization.Serializable

@Serializable
data class SubmissionDTO(
    val submissionId: String? = null,
    val acceptedDocuments: List<DocumentDTO>? = emptyList(),
    val rejectedDocuments: List<RejectedDocumentDTO>,
    val header: HeaderDTO
)