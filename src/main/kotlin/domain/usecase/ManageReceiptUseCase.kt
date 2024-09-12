package domain.usecase

import domain.entity.Receipt
import domain.gateway.IReceiptGateway

class ManageReceiptUseCase(
    private val receiptGateway: IReceiptGateway
) {
    suspend fun getAllReceipts(storeId: Int, dateFrom: String, dateTo: String): List<Receipt> =
        receiptGateway.getAllReceipts(storeId, dateFrom, dateTo)
}