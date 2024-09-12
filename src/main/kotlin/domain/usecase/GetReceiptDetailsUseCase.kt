package domain.usecase

import domain.gateway.remote.IGetReceiptDetails

class GetReceiptDetailsUseCase(
    private val getReceiptDetails: IGetReceiptDetails
) {
    suspend operator fun invoke(uuid: String) = getReceiptDetails.getReceiptDetails(uuid)
}