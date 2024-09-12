package domain.usecase

import domain.gateway.IAuthGateway

class CreateTokenUseCase(
    private val authGateway: IAuthGateway
) {
    suspend operator fun invoke(storeId:Int): String {
        val store = authGateway.getStoreCardEntails(storeId)
        return authGateway.createToken(
            store.clientId ?: "",
            store.clientSecret ?: "",
            store.posSerial ?: "",
            store.posOsVersion ?: ""
        )
    }
}