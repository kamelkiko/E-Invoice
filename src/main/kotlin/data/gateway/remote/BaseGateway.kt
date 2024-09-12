package data.gateway.remote

import domain.util.NetworkException
import domain.util.ServerErrorException
import domain.util.UnknownErrorException
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.statement.*
import io.ktor.utils.io.errors.*

open class BaseGateway(val client: HttpClient) {
    suspend inline fun <reified T> tryToExecute(
        handleError: (ClientRequestException) -> T,
        method: HttpClient.() -> HttpResponse
    ): T {
        try {
            return client.method().body()
        } catch (e: ClientRequestException) {
            return handleError(e)
        } catch (e: RedirectResponseException) {
            val errorMessages = e.message
            throw UnknownErrorException(errorMessages)
        } catch (e: ServerResponseException) {
            val errorMessages = e.message
            throw ServerErrorException(errorMessages)
        } catch (e: IOException) {
            val errorMessages = e.message.toString()
            throw NetworkException(errorMessages)
        } catch (e: Exception) {
            val errorMessages = e.message.toString()
            throw UnknownErrorException(errorMessages)
        }
    }
}