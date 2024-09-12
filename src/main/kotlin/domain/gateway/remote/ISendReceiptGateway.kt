package domain.gateway.remote

import data.remote.model.submit.SubmissionDTO
import domain.entity.Receipt

interface ISendReceiptGateway {
    suspend fun sendReceipt(receipt: List<Receipt>): SubmissionDTO
}