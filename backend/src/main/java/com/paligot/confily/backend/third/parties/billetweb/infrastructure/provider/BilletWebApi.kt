package com.paligot.confily.backend.third.parties.billetweb.infrastructure.provider

import com.paligot.confily.models.Attendee
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.java.Java
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

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

    suspend fun fetchAttendee(
        eventId: String,
        basic: String,
        barcode: String
    ): List<Attendee> {
        val response = client.get("$baseUrl/event/$eventId/attendees?version=1&past=1&barcode=$barcode") {
            headers[HttpHeaders.Authorization] = basic
        }
        if (!response.status.isSuccess()) {
            return emptyList()
        }
        val formatter = Json {
            isLenient = true
            ignoreUnknownKeys = true
        }
        return formatter.decodeFromString(response.body())
    }

    object Factory {
        fun create(enableNetworkLogs: Boolean): BilletWebApi =
            BilletWebApi(
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
