package com.paligot.confily.core.api

import com.paligot.confily.core.api.exceptions.AgendaNotModifiedException
import com.paligot.confily.models.AgendaV4
import com.paligot.confily.models.Attendee
import com.paligot.confily.models.CreatedMap
import com.paligot.confily.models.EventList
import com.paligot.confily.models.EventMap
import com.paligot.confily.models.EventV4
import com.paligot.confily.models.PartnersActivities
import com.paligot.confily.models.QuestionAndResponse
import com.paligot.confily.models.TeamMember
import com.paligot.confily.models.inputs.MapInput
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.accept
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.etag
import io.ktor.http.ifNoneMatch
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

expect val httpClientEngine: HttpClientEngine

class ConferenceApi(
    private val client: HttpClient,
    private val baseUrl: String,
    private val acceptLanguage: String
) {
    suspend fun fetchEventList(): EventList = client.get("$baseUrl/events").body()

    suspend fun fetchEvent(eventId: String): EventV4 {
        val response = client.get("$baseUrl/events/$eventId") {
            contentType(ContentType.parse("application/json"))
            accept(ContentType.parse("application/json; version=4"))
        }
        return response.body()
    }

    suspend fun fetchQAndA(eventId: String): List<QuestionAndResponse> =
        client.get("$baseUrl/events/$eventId/qanda") {
            headers["Accept-Language"] = acceptLanguage
        }.body()

    suspend fun fetchPartnersActivities(eventId: String): PartnersActivities =
        client.get("$baseUrl/events/$eventId/partners/activities").body()

    suspend fun fetchAttendee(eventId: String, barcode: String): Attendee =
        client.get("$baseUrl/events/$eventId/billet-web/$barcode").body()

    suspend fun fetchAgenda(eventId: String, etag: String?): Pair<String, AgendaV4> {
        val response = client.get("$baseUrl/events/$eventId/agenda") {
            contentType(ContentType.parse("application/json"))
            accept(ContentType.parse("application/json; version=4"))
            etag?.let { ifNoneMatch(etag) }
        }
        if (response.status == HttpStatusCode.NotModified) {
            throw AgendaNotModifiedException()
        }
        return response.etag()!! to response.body()
    }

    suspend fun fetchTeamMembers(eventId: String): Map<String, List<TeamMember>> =
        client.get("$baseUrl/events/$eventId/team-members").body()

    suspend fun fetchMapList(eventId: String): List<EventMap> =
        client.get("$baseUrl/events/$eventId/maps").body()

    suspend fun createMap(
        eventId: String,
        apiKey: String,
        fileName: String,
        mapBytes: ByteArray
    ): CreatedMap = client.post("$baseUrl/events/$eventId/maps") {
        headers["api_key"] = apiKey
        setBody(
            MultiPartFormDataContent(
                formData {
                    append(
                        key = "image",
                        value = mapBytes,
                        headers = Headers.build {
                            append(HttpHeaders.ContentType, "image/png")
                            append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
                        }
                    )
                }
            )
        )
    }.body()

    suspend fun updateMap(
        eventId: String,
        apiKey: String,
        mapId: String,
        input: MapInput
    ): EventMap = client.put("$baseUrl/events/$eventId/maps/$mapId") {
        headers["api_key"] = apiKey
        contentType(ContentType.Application.Json)
        setBody(input)
    }.body()

    suspend fun updateMapPlan(
        eventId: String,
        apiKey: String,
        mapId: String,
        filled: Boolean,
        fileName: String,
        mapBytes: ByteArray
    ): EventMap = client.put("$baseUrl/events/$eventId/maps/$mapId/plan?filled=$filled") {
        headers["api_key"] = apiKey
        setBody(
            MultiPartFormDataContent(
                formData {
                    append(
                        key = "image",
                        value = mapBytes,
                        headers = Headers.build {
                            append(HttpHeaders.ContentType, "image/png")
                            append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
                        }
                    )
                }
            )
        )
    }.body()

    companion object {
        fun create(
            baseUrl: String,
            acceptLanguage: String,
            enableNetworkLogs: Boolean,
            httpEngine: HttpClientEngine = httpClientEngine
        ): ConferenceApi =
            ConferenceApi(
                client = HttpClient(httpEngine) {
                    install(
                        ContentNegotiation
                    ) {
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
                            logger = Logger.SIMPLE
                            level = LogLevel.ALL
                        }
                    }
                },
                baseUrl = baseUrl,
                acceptLanguage = acceptLanguage
            )
    }
}
