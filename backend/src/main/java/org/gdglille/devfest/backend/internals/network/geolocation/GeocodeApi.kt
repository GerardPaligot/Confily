package org.gdglille.devfest.backend.internals.network.geolocation

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
import java.net.URLEncoder

class GeocodeApi(
    private val client: HttpClient,
    private val apiKey: String,
    private val baseUrl: String = "https://maps.googleapis.com/maps/api"
) {
    suspend fun geocode(query: String): Geocode =
        client.get("$baseUrl/geocode/json?address=${URLEncoder.encode(query, "utf-8")}&key=$apiKey").body()

    object Factory {
        fun create(apiKey: String, enableNetworkLogs: Boolean): GeocodeApi =
            GeocodeApi(
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
                },
                apiKey = apiKey
            )
    }
}
