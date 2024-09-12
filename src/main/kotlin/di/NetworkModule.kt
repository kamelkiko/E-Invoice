package di

import data.gateway.remote.authorizationIntercept
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val networkModule = module(createdAtStart = true) {
    single(createdAtStart = true) {
        val client = HttpClient(OkHttp) {
            expectSuccess = true
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
                logger = object : Logger {
                    override fun log(message: String) {
                        println("HTTP Client: $message")
                    }
                }
            }
            install(ContentNegotiation) {
                json(
                    Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                    }
                )
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 15000L
                socketTimeoutMillis = 15000L
                connectTimeoutMillis = 15000L
            }
            defaultRequest {
                header("Content-Type", "application/json")
                url("https://id.eta.gov.eg/")
            }
        }
        authorizationIntercept(client)
        client
    }
}