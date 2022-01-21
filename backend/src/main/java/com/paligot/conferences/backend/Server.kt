package com.paligot.conferences.backend

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.FirestoreOptions
import com.paligot.conferences.backend.database.Database
import com.paligot.conferences.backend.database.DatabaseType
import com.paligot.conferences.backend.events.EventDao
import com.paligot.conferences.backend.events.convertToDb
import com.paligot.conferences.backend.events.registerEventRoutes
import com.paligot.conferences.backend.network.ConferenceHallApi
import com.paligot.conferences.backend.partners.PartnerDao
import com.paligot.conferences.backend.schedulers.ScheduleItemDao
import com.paligot.conferences.backend.schedulers.registerSchedulersRoutes
import com.paligot.conferences.backend.speakers.SpeakerDao
import com.paligot.conferences.backend.speakers.convertToDb
import com.paligot.conferences.backend.speakers.registerSpeakersRoutes
import com.paligot.conferences.backend.talks.TalkDao
import com.paligot.conferences.backend.talks.convertToDb
import com.paligot.conferences.backend.talks.registerTalksRoutes
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers

fun main() {
    val firestore = FirestoreOptions.getDefaultInstance().toBuilder().run {
        if (System.getenv("IS_APPENGINE") != "true") {
            setEmulatorHost("localhost:8081")
        }
        setProjectId("cms4partners-ce427")
        setCredentials(GoogleCredentials.getApplicationDefault())
        build()
    }.service
    val projectName = "conferences4hall"
    val speakerDao = SpeakerDao(
        Database.Factory.create(
            DatabaseType.FirestoreDb(
                firestore = firestore,
                projectName = projectName,
                collectionName = "speakers",
                dispatcher = Dispatchers.IO
            )
        )
    )
    val talkDao = TalkDao(
        Database.Factory.create(
            DatabaseType.FirestoreDb(
                firestore = firestore, projectName = projectName, collectionName = "talks", dispatcher = Dispatchers.IO
            )
        )
    )
    val scheduleItemDao = ScheduleItemDao(
        Database.Factory.create(
            DatabaseType.FirestoreDb(
                firestore = firestore,
                projectName = projectName,
                collectionName = "schedule-items",
                dispatcher = Dispatchers.IO
            )
        )
    )
    val eventDao = EventDao(
        Database.Factory.create(
            DatabaseType.FirestoreDb(
                firestore = firestore,
                projectName = projectName,
                collectionName = projectName,
                dispatcher = Dispatchers.IO
            )
        )
    )
    val partnerDao = PartnerDao(
        Database.Factory.create(
            DatabaseType.FirestoreDb(
                firestore = firestore,
                projectName = projectName,
                collectionName = "companies",
                dispatcher = Dispatchers.IO
            )
        )
    )

    embeddedServer(Netty, 8080) {
        install(ContentNegotiation) {
            json()
        }
        routing {
            post("conference-hall/{eventId}/import") {
                val apiKey = call.request.headers["api_key"]
                val eventId = call.parameters["eventId"]!!
                require(apiKey != null) { "api_key header is required" }
                val conferenceHallApi = ConferenceHallApi.Factory.create(apiKey = apiKey, enableNetworkLogs = true)
                val event = conferenceHallApi.fetchEvent(eventId)
                speakerDao.insertAll(eventId, event.speakers.map { it.convertToDb() })
                talkDao.insertAll(eventId, event.talks.map { it.convertToDb(event.categories, event.formats) })
                eventDao.createOrUpdate(event.convertToDb(eventId))
                call.respond(HttpStatusCode.Created, event)
            }
            route("/events/{eventId}") {
                registerEventRoutes(eventDao, speakerDao, talkDao, scheduleItemDao, partnerDao)
                registerSpeakersRoutes(eventDao, speakerDao)
                registerTalksRoutes(eventDao, speakerDao, talkDao)
                registerSchedulersRoutes(eventDao, talkDao, speakerDao, scheduleItemDao)
            }
        }
    }.start(wait = true)
}