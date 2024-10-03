package data.gateway

import data.gateway.remote.BaseGateway
import data.local.model.StoreAuth
import data.remote.model.auth.AuthResponse
import data.util.getData
import domain.gateway.IAuthGateway
import domain.gateway.local.ILocalConfigurationGateway
import domain.util.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.util.*

class AuthGateway(
    client: HttpClient,
    private val localConfigurationGateway: ILocalConfigurationGateway
) : IAuthGateway,
    BaseGateway(client) {
    @OptIn(InternalAPI::class)
    override suspend fun createToken(
        clientId: String,
        clientSecret: String,
        posSerial: String,
        posOsVersion: String
    ): String {
        return tryToExecute<AuthResponse>(
            handleError = { e ->
                val errorMessages = e.response.body<AuthResponse>().error ?: "Invalid client"
                when (e.response.status.value) {
                    400 -> throw ValidationNetworkException(errorMessages)
                    401 -> throw UnAuthException(errorMessages)
                    404 -> throw NotFoundException(errorMessages)
                    else -> throw UnknownErrorException(errorMessages)
                }
            }
        ) {
            client.post("connect/token") {
                headers {
                    append("posserial", posSerial)
                    append("pososversion", posOsVersion)
                }
                contentType(ContentType.Application.FormUrlEncoded)
                body = FormDataContent(
                    Parameters.build {
                        append("grant_type", "client_credentials")
                        append("client_id", clientId)
                        append("client_secret", clientSecret)
                    }
                )
            }
        }.accessToken?.also { token ->
            localConfigurationGateway.saveToken(token)
        } ?: ""
    }

    @Throws(EmptyDataException::class)
    override suspend fun getStoreCardEntails(storeId: String): StoreAuth {
        return try {
            val list = mutableListOf<StoreAuth>()
            val data = getData("select * from dbo.Store")
            if (data.isSuccess) {
                data.getOrNull()?.forEach { _ ->
                    list.add(
                        StoreAuth(
                            posSerial = "PSH71206",
                            posOsVersion = "Windows",
                            clientId = "ba3f5560-5608-4c55-923d-917cc2966e5a",
                            clientSecret = "195a5190-d9ce-422c-98dc-bc30b4885a21"
                        )
                    )
                } ?: throw EmptyDataException("Empty store")
                list
            } else throw NotFoundException("Store not found")
        } catch (e: Exception) {
            throw UnknownErrorException(e.message)
        }.firstOrNull() ?: throw NotFoundException("Store not found")
    }
}