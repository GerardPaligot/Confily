package com.paligot.confily.backend.third.parties.conferencehall

import com.paligot.confily.backend.internals.SystemEnv
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

class ConferenceHallApi(
    private val client: HttpClient,
    private val baseUrl: String = "https://${SystemEnv.conferenceHallUrl}/api"
) {
    suspend fun fetchEventConfirmed(eventId: String, apiKey: String) =
        client.get("$baseUrl/v1/event/$eventId?key=$apiKey&state=confirmed").body<Event>()

    object Factory {
        fun create(enableNetworkLogs: Boolean): ConferenceHallApi =
            ConferenceHallApi(
                client = HttpClient(Java.create()) {
                    install(ContentNegotiation) {
                        json(
                            Json {
                                isLenient = true
                                ignoreUnknownKeys = true
                            }
                        )
                    }
                    if (enableNetworkLogs) {
                        install(
                            Logging
                        ) {
                            logger = Logger.DEFAULT
                            level = LogLevel.INFO
                        }
                    }
                }
            )
    }
}
