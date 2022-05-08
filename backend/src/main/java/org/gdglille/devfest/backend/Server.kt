package org.gdglille.devfest.backend

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.FirestoreOptions
import com.google.cloud.storage.StorageOptions
import org.gdglille.devfest.backend.conferencehall.registerConferenceHallRoutes
import org.gdglille.devfest.backend.database.Database
import org.gdglille.devfest.backend.database.DatabaseType
import org.gdglille.devfest.backend.events.EventDao
import org.gdglille.devfest.backend.events.registerEventRoutes
import org.gdglille.devfest.backend.partners.PartnerDao
import org.gdglille.devfest.backend.schedulers.ScheduleItemDao
import org.gdglille.devfest.backend.schedulers.registerSchedulersRoutes
import org.gdglille.devfest.backend.speakers.SpeakerDao
import org.gdglille.devfest.backend.speakers.registerSpeakersRoutes
import org.gdglille.devfest.backend.storage.Storage
import org.gdglille.devfest.backend.talks.TalkDao
import org.gdglille.devfest.backend.talks.registerTalksRoutes
import org.gdglille.devfest.models.inputs.Validator
import org.gdglille.devfest.models.inputs.ValidatorException
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import org.gdglille.devfest.backend.partners.registerPartnersRoutes

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
        ),
        Storage.Factory.create(
            storage = storage,
            bucketName = projectName,
            isAppEngine = isAppEngine
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
        install(CORS) {
            anyHost()
        }
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
            exception<NotAuthorized> { call, _ ->
                call.respond(HttpStatusCode.Unauthorized, "Your api key isn't the good one")
            }
        }
        routing {
            registerConferenceHallRoutes(eventDao, speakerDao, talkDao)
            route("/events/{eventId}") {
                registerEventRoutes(eventDao, speakerDao, talkDao, scheduleItemDao, partnerDao)
                registerSpeakersRoutes(eventDao, speakerDao)
                registerTalksRoutes(eventDao, speakerDao, talkDao)
                registerSchedulersRoutes(eventDao, talkDao, speakerDao, scheduleItemDao)
                registerPartnersRoutes(eventDao, partnerDao)
            }
        }
    }.start(wait = true)
}

object NotAuthorized : Throwable()
class NotFoundException(message: String) : Throwable(message)

suspend inline fun <reified T : Validator> ApplicationCall.receiveValidated(): T {
    val input = receive<T>()
    val errors = input.validate()
    if (errors.isNotEmpty()) throw ValidatorException(errors)
    return input
}