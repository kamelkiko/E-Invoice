package data.remote.model.details

import kotlinx.serialization.Serializable

@Serializable
data class DocumentTypeDetails(
    val receiptTypeCategory: String? = null,
    val receiptTypeBase: String? = null,
    val receiptType: String? = null,
    val receiptTypeName: String? = null,
    val receiptTypeNameAr: String? = null,
    val typeVersion: String? = null
)