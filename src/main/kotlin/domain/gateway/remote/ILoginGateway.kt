package domain.gateway.remote

interface ILoginGateway {
    suspend fun loginAdmin(username: String, password: String)
}