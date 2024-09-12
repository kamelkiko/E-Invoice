package domain.usecase

import domain.gateway.local.ILocalConfigurationGateway

class GetIsLoggedInUseCase(
    private val localConfigurationGateway: ILocalConfigurationGateway
) {
    suspend operator fun invoke() = localConfigurationGateway.getIsStayedLoggedIn()
}