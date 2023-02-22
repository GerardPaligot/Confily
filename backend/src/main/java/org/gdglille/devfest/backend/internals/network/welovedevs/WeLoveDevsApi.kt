package org.gdglille.devfest.backend.internals.network.welovedevs

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.java.Java
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

private const val MaxJobs = 10

class WeLoveDevsApi(private val client: HttpClient) {
    suspend fun fetchPublicJobs(
        companyIds: List<String>,
        appId: String,
        apiKey: String,
        page: Int = 0
    ): PublicJobs =
        client.post("https://$appId-dsn.algolia.net/1/indexes/public_jobs/query?page=$page") {
            contentType(ContentType.Application.Json)
            headers {
                this["X-Algolia-API-Key"] = apiKey
                this["X-Algolia-Application-Id"] = appId
            }
            setBody(
                JobQuery(
                    filters = companyIds.joinToString(" OR ") { "smallCompany.id:$it" },
                    hitsPerPage = companyIds.size * MaxJobs
                )
            )
        }.body()

    object Factory {
        fun create(enableNetworkLogs: Boolean): WeLoveDevsApi =
            WeLoveDevsApi(
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
