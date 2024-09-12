package data.gateway.remote

import data.remote.model.details.DocumentDetailsData
import domain.gateway.remote.IGetReceiptDetails
import domain.util.NotFoundException
import domain.util.UnAuthException
import domain.util.UnknownErrorException
import domain.util.ValidationNetworkException
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class GetReceiptDetails(client: HttpClient) : IGetReceiptDetails, BaseGateway(client) {
    override suspend fun getReceiptDetails(uuid: String): DocumentDetailsData {
        return tryToExecute<DocumentDetailsData>(
            handleError = { e ->
                val errorMessages = e.response.body<DocumentDetailsData>().message ?: e.message
                when (e.response.status.value) {
                    400 -> throw ValidationNetworkException(errorMessages)
                    401 -> throw UnAuthException("Please re-active pos first")
                    404 -> throw NotFoundException(errorMessages)
                    else -> throw UnknownErrorException(errorMessages)
                }
            }
        ) {
            client.get {
                url("https://api.invoicing.eta.gov.eg/api/v1/receipts/$uuid/raw")
                contentType(ContentType.Application.Json)
            }
        }
    }
}