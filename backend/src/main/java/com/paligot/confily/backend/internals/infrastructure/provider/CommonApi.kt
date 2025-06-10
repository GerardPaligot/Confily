package com.paligot.confily.backend.internals.infrastructure.provider

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.java.Java
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class CommonApi(
    private val client: HttpClient
) {
    suspend fun fetchByteArray(url: String) = client.get(url).body<ByteArray>()

    object Factory {
        fun create(enableNetworkLogs: Boolean): CommonApi =
            CommonApi(
                client = HttpClient(Java.create()) {
                    this.install(ContentNegotiation) {
                        json(
                            Json {
                                isLenient = true
                                ignoreUnknownKeys = true
                            }
                        )
                    }
                    if (enableNetworkLogs) {
                        this.install(Logging) {
                            logger = Logger.Companion.DEFAULT
                            level = LogLevel.INFO
                        }
                    }
                }
            )
    }
}
