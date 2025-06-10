package com.paligot.confily.backend.menus.infrastructure.api

import com.paligot.confily.backend.internals.infrastructure.ktor.plugins.EventUpdatedAtPlugin
import com.paligot.confily.backend.menus.infrastructure.factory.MenuModule.menuAdminRepository
import com.paligot.confily.models.inputs.LunchMenuInput
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import kotlin.getValue

fun Route.registerAdminMenuRoutes() {
    val menuAdminRepository by menuAdminRepository

    route("/menus") {
        this.install(EventUpdatedAtPlugin)
        put {
            val eventId = call.parameters["eventId"]!!
            val input = call.receive<List<LunchMenuInput>>()
            call.respond(HttpStatusCode.OK, menuAdminRepository.update(eventId, input))
        }
    }
}
