package com.paligot.conferences.backend

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.FirestoreOptions
import com.google.cloud.storage.StorageOptions
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
import com.paligot.conferences.backend.storage.Storage
import com.paligot.conferences.backend.talks.TalkDao
import com.paligot.conferences.backend.talks.convertToDb
import com.paligot.conferences.backend.talks.registerTalksRoutes
import com.paligot.conferences.backend.users.UserDao
import com.paligot.conferences.backend.users.registerUserRoutes
import com.paligot.conferences.models.inputs.Validator
import com.paligot.conferences.models.inputs.ValidatorException
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers

fun main() {
    val gcpProjectId = "cms4partners-ce427"
    val projectName = "conferences4hall"
    val isAppEngine = System.getenv("IS_APPENGINE") == "true"
    val firestore = FirestoreOptions.getDefaultInstance().toBuilder().run {
        if (!isAppEngine) {
            setEmulatorHost("localhost:8081")
        }
        setProjectId(gcpProjectId)
        setCredentials(GoogleCredentials.getApplicationDefault())
        build()
    }.service
    val storage = StorageOptions.getDefaultInstance().toBuilder().run {
        setProjectId(gcpProjectId)
        setCredentials(GoogleCredentials.getApplicationDefault())
        build()
    }.service
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
    val userDao = UserDao(
        Storage.Factory.create(
            storage = storage,
            bucketName = projectName,
            isAppEngine = isAppEngine
        )
    )

    embeddedServer(Netty, 8080) {
        install(ContentNegotiation) {
            json()
        }
        install(StatusPages) {
            exception<ValidatorException> { call, cause ->
                call.respond(HttpStatusCode.BadRequest, cause.errors)
            }
            exception<NotFoundException> { call, cause ->
                call.respond(HttpStatusCode.NotFound, cause.message ?: "")
            }
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
                registerUserRoutes(userDao, eventDao)
            }
        }
    }.start(wait = true)
}

class NotFoundException(message: String): Throwable(message)

suspend inline fun <reified T : Validator> ApplicationCall.receiveValidated(): T {
    val input = receive<T>()
    val errors = input.validate()
    if (errors.isNotEmpty()) throw ValidatorException(errors)
    return input
}