package domain.gateway.remote

import data.remote.model.details.DocumentDetailsData

interface IGetReceiptDetails {
    suspend fun getReceiptDetails(uuid: String): DocumentDetailsData
}