package domain.gateway

import domain.entity.Receipt

interface IReceiptGateway {
    suspend fun getAllReceipts(storeId: Int, dateFrom: String, dateTo: String): List<Receipt>
}