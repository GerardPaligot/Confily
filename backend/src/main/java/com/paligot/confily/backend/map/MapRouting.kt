package com.paligot.confily.backend.map

import com.paligot.confily.backend.map.MapModule.mapRepository
import com.paligot.confily.backend.receiveValidated
import com.paligot.confily.models.inputs.MapInput
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.utils.io.jvm.javaio.toInputStream
import java.io.File

fun Route.registerMapRoutes() {
    val repository by mapRepository

    get("/maps") {
        val eventId = call.parameters["eventId"]!!
        call.respond(repository.list(eventId))
    }

    post("/maps") {
        val eventId = call.parameters["eventId"]!!
        val apiKey = call.request.headers["api_key"]!!
        val multipartData = call.receiveMultipart()
        call.respond(
            status = HttpStatusCode.Created,
            message = repository.create(eventId, apiKey, multipartData.readPart().asFile())
        )
    }

    put("/maps/{mapId}") {
        val eventId = call.parameters["eventId"]!!
        val apiKey = call.request.headers["api_key"]!!
        val id = call.parameters["mapId"]!!
        val input = call.receiveValidated<MapInput>()
        call.respond(
            status = HttpStatusCode.OK,
            message = repository.update(eventId, apiKey, id, input)
        )
    }

    put("/maps/{mapId}/plan") {
        val eventId = call.parameters["eventId"]!!
        val apiKey = call.request.headers["api_key"]!!
        val id = call.parameters["mapId"]!!
        val filled = call.request.queryParameters["filled"] == "true"
        val multipartData = call.receiveMultipart()
        call.respond(
            status = HttpStatusCode.OK,
            message = repository.updatePlan(
                eventId = eventId,
                apiKey = apiKey,
                mapId = id,
                file = multipartData.readPart().asFile(),
                filled = filled
            )
        )
    }

    delete("/maps/{mapId}") {
        val eventId = call.parameters["eventId"]!!
        val apiKey = call.request.headers["api_key"]!!
        val id = call.parameters["mapId"]!!
        repository.delete(eventId, apiKey, id)
        call.respond(HttpStatusCode.NoContent)
    }
}

fun PartData?.asFile(): File {
    if (this !is PartData.FileItem) {
        throw BadRequestException("PartData is not a file")
    }
    try {
        val fileName = this.originalFileName ?: "uploaded_file"
        val file = File(fileName)
        this.provider().toInputStream().use { input ->
            file.outputStream().buffered().use { output ->
                input.copyTo(output)
            }
        }
        return file
    } finally {
        this.dispose()
    }
}
