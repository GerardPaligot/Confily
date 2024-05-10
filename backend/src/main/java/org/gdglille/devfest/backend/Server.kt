package org.gdglille.devfest.backend

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.auth.http.HttpCredentialsAdapter
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.FirestoreOptions
import com.google.cloud.secretmanager.v1.SecretManagerServiceClient
import com.google.cloud.secretmanager.v1.SecretManagerServiceSettings
import com.google.cloud.storage.StorageOptions
import io.ktor.http.HeaderValue
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
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
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import org.gdglille.devfest.backend.categories.CategoryDao
import org.gdglille.devfest.backend.categories.registerCategoriesRoutes
import org.gdglille.devfest.backend.events.EventDao
import org.gdglille.devfest.backend.events.registerEventRoutes
import org.gdglille.devfest.backend.formats.FormatDao
import org.gdglille.devfest.backend.formats.registerFormatsRoutes
import org.gdglille.devfest.backend.internals.CommonApi
import org.gdglille.devfest.backend.internals.helpers.database.BasicDatabase
import org.gdglille.devfest.backend.internals.helpers.database.Database
import org.gdglille.devfest.backend.internals.helpers.drive.GoogleDriveDataSource
import org.gdglille.devfest.backend.internals.helpers.image.TranscoderImage
import org.gdglille.devfest.backend.internals.helpers.secret.Secret
import org.gdglille.devfest.backend.internals.helpers.storage.Storage
import org.gdglille.devfest.backend.jobs.JobDao
import org.gdglille.devfest.backend.partners.PartnerDao
import org.gdglille.devfest.backend.partners.registerPartnersRoutes
import org.gdglille.devfest.backend.qanda.QAndADao
import org.gdglille.devfest.backend.qanda.registerQAndAsRoutes
import org.gdglille.devfest.backend.schedules.ScheduleItemDao
import org.gdglille.devfest.backend.schedules.registerSchedulersRoutes
import org.gdglille.devfest.backend.sessions.SessionDao
import org.gdglille.devfest.backend.speakers.SpeakerDao
import org.gdglille.devfest.backend.speakers.registerSpeakersRoutes
import org.gdglille.devfest.backend.talks.registerTalksRoutes
import org.gdglille.devfest.backend.third.parties.billetweb.registerBilletWebRoutes
import org.gdglille.devfest.backend.third.parties.cms4partners.registerCms4PartnersRoutes
import org.gdglille.devfest.backend.third.parties.conferencehall.ConferenceHallApi
import org.gdglille.devfest.backend.third.parties.conferencehall.registerConferenceHallRoutes
import org.gdglille.devfest.backend.third.parties.geocode.GeocodeApi
import org.gdglille.devfest.backend.third.parties.openplanner.OpenPlannerApi
import org.gdglille.devfest.backend.third.parties.openplanner.registerOpenPlannerRoutes
import org.gdglille.devfest.backend.third.parties.welovedevs.WeLoveDevsApi
import org.gdglille.devfest.backend.third.parties.welovedevs.registerWLDRoutes
import org.gdglille.devfest.models.Session
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
    val cloudStorage = StorageOptions.getDefaultInstance().toBuilder().run {
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
    val driveService = Drive.Builder(
        GoogleNetHttpTransport.newTrustedTransport(),
        GsonFactory.getDefaultInstance(),
        HttpCredentialsAdapter(
            GoogleCredentials.getApplicationDefault().createScoped(setOf(DriveScopes.DRIVE))
        )
    )
        .setApplicationName(gcpProjectId)
        .build()
    val database = Database.Factory.create(firestore = firestore, projectName = projectName)
    val basicDatabase = BasicDatabase.Factory.create(firestore = firestore)
    val storage = Storage.Factory.create(
        storage = cloudStorage,
        bucketName = projectName,
        isAppEngine = isCloud
    )
    val speakerDao = SpeakerDao(database, storage)
    val sessionDao = SessionDao(database)
    val categoryDao = CategoryDao(database)
    val formatDao = FormatDao(database)
    val scheduleItemDao = ScheduleItemDao(database)
    val eventDao = EventDao(projectName, basicDatabase)
    val partnerDao = PartnerDao(database, storage)
    val jobDao = JobDao(database)
    val qAndADao = QAndADao(database)
    val driveDataSource = GoogleDriveDataSource(driveService)
    val wldApi = WeLoveDevsApi.Factory.create(enableNetworkLogs = true)
    val geocodeApi = GeocodeApi.Factory.create(
        apiKey = secret["GEOCODE_API_KEY"],
        enableNetworkLogs = true
    )
    val openPlannerApi = OpenPlannerApi.Factory.create(enableNetworkLogs = true)
    val conferenceHallApi = ConferenceHallApi.Factory.create(enableNetworkLogs = true)
    val commonApi = CommonApi.Factory.create(enableNetworkLogs = true)
    val imageTranscoder = TranscoderImage()
    embeddedServer(Netty, PORT) {
        install(CORS) {
            allowMethod(HttpMethod.Options)
            allowMethod(HttpMethod.Post)
            allowMethod(HttpMethod.Put)
            allowMethod(HttpMethod.Get)
            allowMethod(HttpMethod.Delete)
            allowHeader(HttpHeaders.AccessControlAllowOrigin)
            allowHeader(HttpHeaders.ContentType)
            allowHeader(HttpHeaders.Authorization)
            anyHost()
        }
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    serializersModule = SerializersModule {
                        polymorphic(Session::class) {
                            this.subclass(Session.Talk::class)
                            this.subclass(Session.Event::class)
                        }
                    }
                }
            )
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
            registerEventRoutes(
                geocodeApi,
                eventDao,
                speakerDao,
                qAndADao,
                sessionDao,
                categoryDao,
                formatDao,
                scheduleItemDao,
                partnerDao
            )
            route("/events/{eventId}") {
                registerQAndAsRoutes(eventDao, qAndADao)
                registerSpeakersRoutes(commonApi, eventDao, speakerDao)
                registerTalksRoutes(
                    eventDao,
                    speakerDao,
                    sessionDao,
                    categoryDao,
                    formatDao,
                    driveDataSource
                )
                registerCategoriesRoutes(eventDao, categoryDao)
                registerFormatsRoutes(eventDao, formatDao)
                registerSchedulersRoutes(
                    eventDao,
                    sessionDao,
                    categoryDao,
                    formatDao,
                    speakerDao,
                    scheduleItemDao
                )
                registerPartnersRoutes(geocodeApi, eventDao, partnerDao, jobDao, imageTranscoder)
                // Third parties
                registerBilletWebRoutes(eventDao)
                registerCms4PartnersRoutes(
                    geocodeApi,
                    eventDao,
                    partnerDao,
                    jobDao,
                    imageTranscoder
                )
                registerConferenceHallRoutes(
                    conferenceHallApi,
                    commonApi,
                    eventDao,
                    speakerDao,
                    sessionDao,
                    categoryDao,
                    formatDao
                )
                registerOpenPlannerRoutes(
                    openPlannerApi,
                    commonApi,
                    eventDao,
                    speakerDao,
                    sessionDao,
                    categoryDao,
                    formatDao,
                    scheduleItemDao
                )
                registerWLDRoutes(wldApi, eventDao, partnerDao, jobDao)
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
