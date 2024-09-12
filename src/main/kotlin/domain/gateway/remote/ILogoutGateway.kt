package domain.gateway.remote

interface ILogoutGateway {
    suspend fun logoutAdmin()
}