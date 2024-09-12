package domain.usecase

import domain.gateway.remote.ILogoutGateway

class LogoutUserUseCase(private val loginLocalGateway: ILogoutGateway) {

    suspend fun logoutUser() {
        loginLocalGateway.logoutAdmin()
    }

}