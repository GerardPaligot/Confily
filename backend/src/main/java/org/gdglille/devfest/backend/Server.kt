package org.gdglille.devfest.backend

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.FirestoreOptions
import com.google.cloud.secretmanager.v1.SecretManagerServiceClient
import com.google.cloud.secretmanager.v1.SecretManagerServiceSettings
import com.google.cloud.storage.StorageOptions
import io.ktor.http.HeaderValue
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.conditionalheaders.ConditionalHeaders
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import org.gdglille.devfest.backend.billetweb.registerBilletWebRoutes
import org.gdglille.devfest.backend.conferencehall.registerConferenceHallRoutes
import org.gdglille.devfest.backend.events.EventDao
import org.gdglille.devfest.backend.events.registerEventRoutes
import org.gdglille.devfest.backend.internals.helpers.database.BasicDatabase
import org.gdglille.devfest.backend.internals.helpers.database.Database
import org.gdglille.devfest.backend.internals.helpers.secret.Secret
import org.gdglille.devfest.backend.internals.helpers.storage.Storage
import org.gdglille.devfest.backend.internals.network.conferencehall.ConferenceHallApi
import org.gdglille.devfest.backend.internals.network.geolocation.GeocodeApi
import org.gdglille.devfest.backend.internals.network.welovedevs.WeLoveDevsApi
import org.gdglille.devfest.backend.jobs.JobDao
import org.gdglille.devfest.backend.jobs.registerJobRoutes
import org.gdglille.devfest.backend.partners.PartnerDao
import org.gdglille.devfest.backend.partners.cms4partners.Cms4PartnersDao
import org.gdglille.devfest.backend.partners.registerPartnersRoutes
import org.gdglille.devfest.backend.schedulers.ScheduleItemDao
import org.gdglille.devfest.backend.schedulers.registerSchedulersRoutes
import org.gdglille.devfest.backend.speakers.SpeakerDao
import org.gdglille.devfest.backend.speakers.registerSpeakersRoutes
import org.gdglille.devfest.backend.talks.TalkDao
import org.gdglille.devfest.backend.talks.registerTalksRoutes
import org.gdglille.devfest.models.inputs.Validator
import org.gdglille.devfest.models.inputs.ValidatorException

const val PORT = 8080

@Suppress("LongMethod")
fun main() {
    val projectName = "conferences4hall"
    val gcpProjectId = System.getenv("PROJECT_ID")
    val isCloud = System.getenv("IS_CLOUD") == "true"
    val firestore = FirestoreOptions.getDefaultInstance().toBuilder().run {
        if (!isCloud) {
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
    val secret = Secret.Factory.create(
        projectId = gcpProjectId,
        client = SecretManagerServiceClient.create(
            SecretManagerServiceSettings.newBuilder().apply {
                this.credentialsProvider =
                    SecretManagerServiceSettings.defaultCredentialsProviderBuilder().build()
            }.build()
        )
    )
    val database = Database.Factory.create(firestore = firestore, projectName = projectName)
    val basicDatabase = BasicDatabase.Factory.create(firestore = firestore)
    val speakerDao = SpeakerDao(
        database,
        Storage.Factory.create(
            storage = storage,
            bucketName = projectName,
            isAppEngine = isCloud
        )
    )
    val talkDao = TalkDao(database)
    val scheduleItemDao = ScheduleItemDao(database)
    val eventDao = EventDao(projectName, basicDatabase)
    val partnerDao = PartnerDao(database)
    val cms4partnerDao = Cms4PartnersDao(basicDatabase)
    val jobDao = JobDao(database)
    val wldApi = WeLoveDevsApi.Factory.create(enableNetworkLogs = true)
    val geocodeApi = GeocodeApi.Factory.create(
        apiKey = secret["GEOCODE_API_KEY"],
        enableNetworkLogs = true
    )
    val conferenceHallApi = ConferenceHallApi.Factory.create(enableNetworkLogs = true)
    embeddedServer(Netty, PORT) {
        install(CORS) {
            anyHost()
        }
        install(ContentNegotiation) {
            json()
        }
        install(ConditionalHeaders)
        install(StatusPages) {
            exception<ValidatorException> { call, cause ->
                call.respond(HttpStatusCode.BadRequest, cause.errors)
            }
            exception<NotFoundException> { call, cause ->
                call.respond(HttpStatusCode.NotFound, cause.message ?: "")
            }
            exception<NotAcceptableException> { call, cause ->
                call.respond(HttpStatusCode.NotAcceptable, cause.message ?: "")
            }
            exception<NotAuthorized> { call, _ ->
                call.respond(HttpStatusCode.Unauthorized, "Your api key isn't the good one")
            }
        }
        routing {
            registerEventRoutes(geocodeApi, eventDao, speakerDao, talkDao, scheduleItemDao, partnerDao, cms4partnerDao)
            route("/events/{eventId}") {
                registerConferenceHallRoutes(conferenceHallApi, eventDao, speakerDao, talkDao)
                registerSpeakersRoutes(eventDao, speakerDao)
                registerTalksRoutes(eventDao, speakerDao, talkDao)
                registerSchedulersRoutes(eventDao, talkDao, speakerDao, scheduleItemDao)
                registerPartnersRoutes(geocodeApi, eventDao, partnerDao, cms4partnerDao, jobDao)
                registerBilletWebRoutes(eventDao)
                registerJobRoutes(wldApi, eventDao, partnerDao, cms4partnerDao, jobDao)
            }
        }
    }.start(wait = true)
}

object NotAuthorized : Throwable()
class NotFoundException(message: String) : Throwable(message)
class NotAcceptableException(message: String) : Throwable(message)

@Suppress("ReturnCount")
fun List<HeaderValue>.version(): Int {
    val header = this.find { it.value == "application/json" } ?: return 1
    val param = header.params.find { it.name == "version" } ?: return 1
    return param.value.toInt()
}

suspend inline fun <reified T : Validator> ApplicationCall.receiveValidated(): T {
    val input = receive<T>()
    val errors = input.validate()
    if (errors.isNotEmpty()) throw ValidatorException(errors)
    return input
}
