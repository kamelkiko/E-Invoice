package data.gateway.fake

import domain.gateway.remote.ILoginGateway
import domain.util.NotFoundException

class FakeLoginGateway : ILoginGateway {
    override suspend fun loginAdmin(username: String, password: String) {
        if (username == USERNAME && password == PASSWORD) return else throw NotFoundException("User not found")
    }

    companion object {
        private const val USERNAME = "1"
        private const val PASSWORD = "1"
    }
}