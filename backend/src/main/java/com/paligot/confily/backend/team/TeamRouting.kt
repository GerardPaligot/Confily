package com.paligot.confily.backend.team

import com.paligot.confily.backend.internals.infrastructure.ktor.plugins.EventUpdatedAtPlugin
import com.paligot.confily.backend.receiveValidated
import com.paligot.confily.backend.team.TeamModule.teamRepository
import com.paligot.confily.models.inputs.TeamMemberInput
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route

fun Route.registerTeamRoutes() {
    val repository by teamRepository

    get("/team-members") {
        val eventId = call.parameters["eventId"]!!
        call.respond(HttpStatusCode.OK, repository.list(eventId))
    }
}

fun Route.registerAdminTeamRoutes() {
    val repository by teamRepository

    route("/team-members") {
        this.install(EventUpdatedAtPlugin)
        post {
            val eventId = call.parameters["eventId"]!!
            val teamMemberInput = call.receiveValidated<TeamMemberInput>()
            call.respond(HttpStatusCode.Created, repository.create(eventId, teamMemberInput))
        }
        put("/{id}") {
            val eventId = call.parameters["eventId"]!!
            val teamMemberId = call.parameters["id"]!!
            val teamMemberInput = call.receiveValidated<TeamMemberInput>()
            call.respond(
                status = HttpStatusCode.OK,
                message = repository.update(
                    eventId = eventId,
                    teamMemberId = teamMemberId,
                    input = teamMemberInput
                )
            )
        }
    }
}
