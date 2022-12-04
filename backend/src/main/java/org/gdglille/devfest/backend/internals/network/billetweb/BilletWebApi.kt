package org.gdglille.devfest.backend.internals.network.billetweb

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
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.gdglille.devfest.models.Attendee

class BilletWebApi(
    private val client: HttpClient,
    private val baseUrl: String = "https://www.billetweb.fr/api"
) {
    suspend fun fetchAttendee(
        eventId: String,
        userId: String,
        apiKey: String,
        barcode: String
    ): List<Attendee> {
        val body =
            client.get("$baseUrl/event/$eventId/attendees?user=$userId&key=$apiKey&version=1&past=1&barcode=$barcode")
                .body<String>()
        val formatter = Json {
            isLenient = true
            ignoreUnknownKeys = true
        }
        return formatter.decodeFromString(body)
    }

    object Factory {
        fun create(enableNetworkLogs: Boolean): BilletWebApi =
            BilletWebApi(
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
