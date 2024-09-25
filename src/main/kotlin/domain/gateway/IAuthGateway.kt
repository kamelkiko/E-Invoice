package domain.gateway

import data.local.model.StoreAuth

interface IAuthGateway {
    suspend fun createToken(
        clientId: String,
        clientSecret: String,
        posSerial: String,
        posOsVersion: String,
    ): String

    suspend fun getStoreCardEntails(
        storeId: String,
    ): StoreAuth
}