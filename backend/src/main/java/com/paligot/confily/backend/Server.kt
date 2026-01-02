package com.paligot.confily.backend

import com.paligot.confily.backend.activities.infrastructure.api.registerAdminActivitiesRoutes
import com.paligot.confily.backend.categories.infrastructure.api.registerAdminCategoriesRoutes
import com.paligot.confily.backend.categories.infrastructure.api.registerCategoriesRoutes
import com.paligot.confily.backend.events.infrastructure.api.registerAdminEventRoutes
import com.paligot.confily.backend.events.infrastructure.api.registerEventRoutes
import com.paligot.confily.backend.export.infrastructure.api.registerAdminExportRoutes
import com.paligot.confily.backend.export.infrastructure.api.registerExportRoutes
import com.paligot.confily.backend.formats.infrastructure.api.registerAdminFormatsRoutes
import com.paligot.confily.backend.formats.infrastructure.api.registerFormatsRoutes
import com.paligot.confily.backend.integrations.infrastructure.api.integrationRoutes
import com.paligot.confily.backend.internals.infrastructure.exposed.DatabaseFactory
import com.paligot.confily.backend.internals.infrastructure.exposed.PostgresModule
import com.paligot.confily.backend.internals.infrastructure.ktor.http.ExceptionMessage
import com.paligot.confily.backend.internals.infrastructure.system.SystemEnv
import com.paligot.confily.backend.map.infrastructure.api.registerAdminMapRoutes
import com.paligot.confily.backend.map.infrastructure.api.registerMapRoutes
import com.paligot.confily.backend.menus.infrastructure.api.registerAdminMenuRoutes
import com.paligot.confily.backend.partners.infrastructure.api.registerAdminPartnersRoutes
import com.paligot.confily.backend.partners.infrastructure.api.registerAdminWLDRoutes
import com.paligot.confily.backend.partners.infrastructure.api.registerPartnersRoutes
import com.paligot.confily.backend.planning.infrastructure.api.registerPlanningRoutes
import com.paligot.confily.backend.qanda.infrastructure.api.registerAdminQAndAsRoutes
import com.paligot.confily.backend.qanda.infrastructure.api.registerQAndAsRoutes
import com.paligot.confily.backend.schedules.infrastructure.api.registerAdminSchedulersRoutes
import com.paligot.confily.backend.schedules.infrastructure.api.registerSchedulersRoutes
import com.paligot.confily.backend.sessions.infrastructure.api.registerAdminSessionsRoutes
import com.paligot.confily.backend.sessions.infrastructure.api.registerSessionsRoutes
import com.paligot.confily.backend.speakers.infrastructure.api.registerAdminSpeakersRoutes
import com.paligot.confily.backend.speakers.infrastructure.api.registerSpeakersRoutes
import com.paligot.confily.backend.tags.infrastructure.api.registerAdminTagsRoutes
import com.paligot.confily.backend.tags.infrastructure.api.registerTagsRoutes
import com.paligot.confily.backend.team.infrastructure.api.registerAdminTeamRoutes
import com.paligot.confily.backend.team.infrastructure.api.registerTeamRoutes
import com.paligot.confily.backend.third.parties.billetweb.infrastructure.api.registerBilletWebRoutes
import com.paligot.confily.backend.third.parties.cms4conference.infrastructure.api.registerAdminCms4PartnersRoutes
import com.paligot.confily.backend.third.parties.openfeedback.infrastructure.api.registerOpenfeedackRoutes
import com.paligot.confily.backend.third.parties.openplanner.infrastructure.api.registerAdminOpenPlannerRoutes
import com.paligot.confily.models.Session
import com.paligot.confily.models.inputs.Validator
import com.paligot.confily.models.inputs.ValidatorException
import io.ktor.http.HeaderValue
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
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
import org.jetbrains.exposed.dao.exceptions.EntityNotFoundException

const val PORT = 8080

@Suppress("LongMethod")
fun main() {
    embeddedServer(
        factory = Netty,
        port = PORT,
        module = Application::module
    ).start(wait = true)
}

fun Application.module(config: ApplicationConfig = ApplicationConfig()) {
    if (SystemEnv.DatabaseConfig.hasPostgres) {
        PostgresModule.init(DatabaseFactory.init(applicationConfig = config))
    }
    cors()
    serialization()
    install(ConditionalHeaders)
    exceptions()
    routing()
}

private fun Application.cors() {
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
}

private fun Application.serialization() {
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
}

private fun Application.exceptions() {
    install(StatusPages) {
        exception<ValidatorException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, ExceptionMessage(cause.message ?: "", cause.errors))
        }
        exception<NotAuthorized> { call, _ ->
            call.respond(HttpStatusCode.Unauthorized, ExceptionMessage("Your api key isn't the good one"))
        }
        exception<ForbiddenException> { call, cause ->
            call.respond(HttpStatusCode.Forbidden, ExceptionMessage(cause.message ?: ""))
        }
        exception<NotFoundException> { call, cause ->
            call.respond(HttpStatusCode.NotFound, ExceptionMessage(cause.message ?: ""))
        }
        exception<EntityNotFoundException> { call, cause ->
            call.respond(HttpStatusCode.NotFound, ExceptionMessage(cause.message ?: ""))
        }
        exception<NotAcceptableException> { call, cause ->
            call.respond(HttpStatusCode.NotAcceptable, ExceptionMessage(cause.message ?: ""))
        }
        exception<ConflictException> { call, cause ->
            call.respond(HttpStatusCode.Conflict, ExceptionMessage(cause.message ?: ""))
        }
    }
}

private fun Application.routing() {
    routing {
        registerEventRoutes()
        route("/events/{eventId}") {
            registerPlanningRoutes()
            registerMapRoutes()
            registerQAndAsRoutes()
            registerSpeakersRoutes()
            registerSessionsRoutes()
            registerCategoriesRoutes()
            registerFormatsRoutes()
            registerTagsRoutes()
            registerSchedulersRoutes()
            registerPartnersRoutes()
            registerTeamRoutes()
            registerExportRoutes()
            // Third parties
            registerOpenfeedackRoutes()
            registerBilletWebRoutes()
        }
        route("/admin") {
            // this.install(IdentificationPlugin)
            // this.install(UpdatedAtPlugin)
            registerAdminEventRoutes()
            route("/events/{eventId}") {
                registerAdminActivitiesRoutes()
                registerAdminCategoriesRoutes()
                registerAdminFormatsRoutes()
                registerAdminMapRoutes()
                registerAdminMenuRoutes()
                registerAdminPartnersRoutes()
                registerAdminQAndAsRoutes()
                registerAdminSchedulersRoutes()
                registerAdminSpeakersRoutes()
                registerAdminTagsRoutes()
                registerAdminSessionsRoutes()
                registerAdminTeamRoutes()
                registerAdminExportRoutes()
                // Third parties
                integrationRoutes()
                registerAdminCms4PartnersRoutes()
                registerAdminOpenPlannerRoutes()
                registerAdminWLDRoutes()
            }
        }
    }
}

object NotAuthorized : Throwable()
class NotFoundException(message: String) : Throwable(message)
class ForbiddenException(message: String) : Throwable(message)
class NotAcceptableException(message: String) : Throwable(message)
class ConflictException(message: String) : Throwable(message)

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
