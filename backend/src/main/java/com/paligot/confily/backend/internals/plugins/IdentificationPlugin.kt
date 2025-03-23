package com.paligot.confily.backend.internals.plugins

import com.paligot.confily.backend.NotAuthorized
import com.paligot.confily.backend.events.EventModule.eventDao
import io.ktor.server.application.createRouteScopedPlugin
import io.ktor.server.plugins.BadRequestException

val IdentificationPlugin = createRouteScopedPlugin("IdentificationPlugin") {
    val eventDao by eventDao

    onCall { call ->
        val eventId = call.parameters["eventId"]
            ?: throw BadRequestException("Missing eventId")
        val apiKey = call.request.headers["x-api-key"]
            ?: call.request.queryParameters["api_key"]
            ?: throw BadRequestException("Missing api key")
        val eventDb = eventDao.get(eventId)
        if (eventDb.apiKey != apiKey) {
            throw NotAuthorized
        }
    }
}
