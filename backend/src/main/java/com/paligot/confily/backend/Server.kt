package com.paligot.confily.backend

import com.paligot.confily.backend.activities.infrastructure.api.registerAdminActivitiesRoutes
import com.paligot.confily.backend.categories.infrastructure.api.registerAdminCategoriesRoutes
import com.paligot.confily.backend.categories.infrastructure.api.registerCategoriesRoutes
import com.paligot.confily.backend.events.infrastructure.api.registerAdminEventRoutes
import com.paligot.confily.backend.events.infrastructure.api.registerEventRoutes
import com.paligot.confily.backend.export.registerAdminExportRoutes
import com.paligot.confily.backend.export.registerExportRoutes
import com.paligot.confily.backend.formats.infrastructure.api.registerAdminFormatsRoutes
import com.paligot.confily.backend.formats.infrastructure.api.registerFormatsRoutes
import com.paligot.confily.backend.internals.infrastructure.ktor.plugins.IdentificationPlugin
import com.paligot.confily.backend.internals.infrastructure.ktor.plugins.UpdatedAtPlugin
import com.paligot.confily.backend.map.infrastructure.api.registerAdminMapRoutes
import com.paligot.confily.backend.map.infrastructure.api.registerMapRoutes
import com.paligot.confily.backend.menus.infrastructure.api.registerAdminMenuRoutes
import com.paligot.confily.backend.partners.registerAdminPartnersRoutes
import com.paligot.confily.backend.partners.registerPartnersRoutes
import com.paligot.confily.backend.planning.infrastructure.api.registerPlanningRoutes
import com.paligot.confily.backend.qanda.registerAdminQAndAsRoutes
import com.paligot.confily.backend.qanda.registerQAndAsRoutes
import com.paligot.confily.backend.schedules.registerAdminSchedulersRoutes
import com.paligot.confily.backend.schedules.registerSchedulersRoutes
import com.paligot.confily.backend.sessions.registerAdminSessionsRoutes
import com.paligot.confily.backend.sessions.registerSessionsRoutes
import com.paligot.confily.backend.speakers.registerAdminSpeakersRoutes
import com.paligot.confily.backend.speakers.registerSpeakersRoutes
import com.paligot.confily.backend.tags.registerAdminTagsRoutes
import com.paligot.confily.backend.tags.registerTagsRoutes
import com.paligot.confily.backend.talks.registerAdminTalksRoutes
import com.paligot.confily.backend.talks.registerTalksRoutes
import com.paligot.confily.backend.team.registerAdminTeamRoutes
import com.paligot.confily.backend.team.registerTeamRoutes
import com.paligot.confily.backend.third.parties.billetweb.registerBilletWebRoutes
import com.paligot.confily.backend.third.parties.cms4partners.registerAdminCms4PartnersRoutes
import com.paligot.confily.backend.third.parties.openfeedback.infrastructure.api.registerOpenfeedackRoutes
import com.paligot.confily.backend.third.parties.openplanner.registerAdminOpenPlannerRoutes
import com.paligot.confily.backend.third.parties.welovedevs.infrastructure.api.registerAdminWLDRoutes
import com.paligot.confily.models.Session
import com.paligot.confily.models.inputs.Validator
import com.paligot.confily.models.inputs.ValidatorException
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

const val PORT = 8080

@Suppress("LongMethod")
fun main() {
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
            registerEventRoutes()
            route("/events/{eventId}") {
                registerPlanningRoutes()
                registerMapRoutes()
                registerQAndAsRoutes()
                registerSpeakersRoutes()
                registerSessionsRoutes()
                registerTalksRoutes()
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
                this.install(IdentificationPlugin)
                this.install(UpdatedAtPlugin)
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
                    registerAdminSessionsRoutes()
                    registerAdminSpeakersRoutes()
                    registerAdminTagsRoutes()
                    registerAdminTalksRoutes()
                    registerAdminTeamRoutes()
                    registerAdminExportRoutes()
                    // Third parties
                    registerAdminCms4PartnersRoutes()
                    registerAdminOpenPlannerRoutes()
                    registerAdminWLDRoutes()
                }
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
