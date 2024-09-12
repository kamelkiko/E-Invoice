package data.gateway.fake

import domain.gateway.remote.ILogoutGateway

class FakeLogoutGateway : ILogoutGateway {
    override suspend fun logoutAdmin() {
        return
    }
}