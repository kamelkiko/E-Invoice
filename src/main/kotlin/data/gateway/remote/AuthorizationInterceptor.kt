package data.gateway.remote

import domain.gateway.local.ILocalConfigurationGateway
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import org.koin.core.scope.Scope

fun Scope.authorizationIntercept(client: HttpClient) {

    val localConfigurationGateway: ILocalConfigurationGateway by inject()

    client.plugin(HttpSend).intercept { request ->
//        val languageCode = localConfigurationGateway.getLanguageCode()
        val token = localConfigurationGateway.getToken()
        request.headers {
            append("Authorization", "Bearer $token")
            // append("Accept-Language", languageCode)
        }

        val originalCall = execute(request)
        originalCall
    }
}