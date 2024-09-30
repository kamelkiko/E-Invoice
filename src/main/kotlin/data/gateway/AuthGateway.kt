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
                            posSerial = "SPAH5Q5EW",
                            posOsVersion = "Windows",
                            clientId = "b3fadf52-bc3a-40db-ab57-09647ab8dc6e",
                            clientSecret = "7d69b416-19f0-43bd-91da-80e806d92c3c"
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