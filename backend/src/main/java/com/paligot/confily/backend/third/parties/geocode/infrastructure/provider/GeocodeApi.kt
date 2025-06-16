package com.paligot.confily.backend.third.parties.geocode.infrastructure.provider

import com.paligot.confily.backend.NotAuthorized
import com.paligot.confily.backend.internals.helpers.secret.Secret
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
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.json.Json
import java.net.URLEncoder

class GeocodeApi(
    private val client: HttpClient,
    private val secret: Secret,
    private val baseUrl: String = "https://maps.googleapis.com/maps/api"
) {
    suspend fun geocode(query: String): Geocode = coroutineScope {
        val encodeQuery = URLEncoder.encode(query, "utf-8")
        val apiKey = secret["GEOCODE_API_KEY"] ?: throw NotAuthorized
        return@coroutineScope client
            .get("$baseUrl/geocode/json?address=$encodeQuery&key=$apiKey")
            .body()
    }

    object Factory {
        fun create(secret: Secret, enableNetworkLogs: Boolean): GeocodeApi =
            GeocodeApi(
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
                            logger = Logger.DEFAULT
                            level = LogLevel.INFO
                        }
                    }
                },
                secret = secret
            )
    }
}
