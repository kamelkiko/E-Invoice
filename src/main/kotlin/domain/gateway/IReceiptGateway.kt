package domain.gateway

import domain.entity.Receipt

interface IReceiptGateway {
    suspend fun getAllReceipts(storeId: String, dateFrom: String, dateTo: String): List<Receipt>
}