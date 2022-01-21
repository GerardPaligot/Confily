package com.paligot.conferences.network

import com.paligot.conferences.Platform
import com.paligot.conferences.models.Agenda
import com.paligot.conferences.models.Event
import com.paligot.conferences.models.ScheduleItem
import com.paligot.conferences.models.Speaker
import com.paligot.conferences.models.Talk
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class ConferenceApi(
    private val client: HttpClient, private val baseUrl: String, private val eventId: String
) {
    suspend fun fetchEvent() = client.get("$baseUrl/events/$eventId").body<Event>()
    suspend fun fetchAgenda() = client.get("$baseUrl/events/$eventId/agenda").body<Agenda>()
    suspend fun fetchSpeaker(speakerId: String) = client.get("$baseUrl/events/$eventId/speakers/$speakerId").body<Speaker>()
    suspend fun fetchSchedule(scheduleId: String) = client.get("$baseUrl/events/$eventId/schedulers/$scheduleId").body<ScheduleItem>()

    object Factory {
        fun create(baseUrl: String, eventId: String, enableNetworkLogs: Boolean): ConferenceApi =
            ConferenceApi(baseUrl = baseUrl, eventId = eventId, client = HttpClient(Platform().engine) {
                install(ContentNegotiation) {
                    json(Json {
                        isLenient = true
                        ignoreUnknownKeys = true
                    })
                }
                if (enableNetworkLogs) {
                    install(Logging) {
                        logger = Logger.DEFAULT
                        level = LogLevel.INFO
                    }
                }
            })
    }
}
