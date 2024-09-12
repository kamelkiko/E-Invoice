package data.gateway.remote

import data.remote.model.submit.SubmissionDTO
import domain.entity.Receipt
import domain.gateway.remote.ISendReceiptGateway
import domain.util.NotFoundException
import domain.util.UnAuthException
import domain.util.UnknownErrorException
import domain.util.ValidationNetworkException
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.util.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class SendReceiptGateway(client: HttpClient) : ISendReceiptGateway, BaseGateway(client) {

    @OptIn(InternalAPI::class)
    override suspend fun sendReceipt(receipt: List<Receipt>): SubmissionDTO {
        return tryToExecute<SubmissionDTO>(
            handleError = { e ->
                when (e.response.status.value) {
                    400 -> throw ValidationNetworkException(e.message)
                    401 -> throw UnAuthException("Please re-active pos first")
                    404 -> throw NotFoundException(e.message)
                    else -> throw UnknownErrorException(e.message)
                }
            }
        ) {
            client.post {
                url("https://api.invoicing.eta.gov.eg/api/v1/receiptsubmissions")
                contentType(ContentType.Application.Json)
                body = Json.encodeToString(SendReceiptRequest.serializer(), SendReceiptRequest(receipt))
            }
        }
    }
}

@Serializable
data class SendReceiptRequest(
    val receipts: List<Receipt>
)