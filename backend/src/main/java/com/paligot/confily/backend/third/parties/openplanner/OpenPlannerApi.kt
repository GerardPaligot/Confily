package com.paligot.confily.backend.third.parties.openplanner

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
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json

class OpenPlannerApi(
    private val client: HttpClient,
    private val baseUrl: String = "https://storage.googleapis.com/conferencecenterr.appspot.com"
) {
    suspend fun fetchPrivateJson(eventId: String, privateId: String): OpenPlanner =
        client.get("$baseUrl/events/$eventId/$privateId.json?t=${Clock.System.now().epochSeconds}")
            .body<OpenPlanner>()

    object Factory {
        fun create(enableNetworkLogs: Boolean): OpenPlannerApi =
            OpenPlannerApi(
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
