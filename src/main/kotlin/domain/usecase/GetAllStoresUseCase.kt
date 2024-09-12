package domain.usecase

import domain.gateway.IStoresGateway

class GetAllStoresUseCase(
    private val storesGateway: IStoresGateway
) {
    suspend operator fun invoke() = storesGateway.getAllStores()
}