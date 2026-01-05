package com.paligot.confily.backend.team.infrastructure.api

import com.paligot.confily.backend.internals.infrastructure.ktor.http.Identifier
import com.paligot.confily.backend.receiveValidated
import com.paligot.confily.backend.team.infrastructure.factory.TeamModule.teamAdminRepository
import com.paligot.confily.backend.team.infrastructure.factory.TeamModule.teamRepository
import com.paligot.confily.models.inputs.TeamMemberInput
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route

fun Route.registerTeamRoutes() {
    val repository = teamRepository

    get("/team-members") {
        val eventId = call.parameters["eventId"]!!
        call.respond(status = HttpStatusCode.OK, message = repository.list(eventId))
    }
}

fun Route.registerAdminTeamRoutes() {
    val repository = teamAdminRepository

    route("/team-members") {
        post {
            val eventId = call.parameters["eventId"]!!
            val teamMemberInput = call.receiveValidated<TeamMemberInput>()
            val id = repository.create(eventId, teamMemberInput)
            call.respond(status = HttpStatusCode.Created, message = Identifier(id))
        }
        put("/{id}") {
            val eventId = call.parameters["eventId"]!!
            val teamMemberId = call.parameters["id"]!!
            val teamMemberInput = call.receiveValidated<TeamMemberInput>()
            val id = repository.update(
                eventId = eventId,
                teamMemberId = teamMemberId,
                input = teamMemberInput
            )
            call.respond(status = HttpStatusCode.OK, message = Identifier(id))
        }
    }
}
