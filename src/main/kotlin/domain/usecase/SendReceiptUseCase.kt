package domain.usecase

import domain.entity.Receipt
import domain.gateway.remote.ISendReceiptGateway

class SendReceiptUseCase(
    private val sendReceiptGateway: ISendReceiptGateway
) {
    suspend operator fun invoke(receipt: List<Receipt>) = sendReceiptGateway.sendReceipt(receipt)
}