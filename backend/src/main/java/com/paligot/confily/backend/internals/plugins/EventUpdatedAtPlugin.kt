package com.paligot.confily.backend.internals.plugins

import com.paligot.confily.backend.events.EventModule.eventDao
import io.ktor.http.HttpMethod
import io.ktor.server.application.createRouteScopedPlugin
import io.ktor.server.request.httpMethod

val EventUpdatedAtPlugin = createRouteScopedPlugin("EventUpdatedAtPlugin") {
    val eventDao by eventDao

    onCallRespond { call ->
        if (call.request.httpMethod == HttpMethod.Post || call.request.httpMethod == HttpMethod.Put) {
            val eventId = call.parameters["eventId"] ?: return@onCallRespond
            eventDao.updateEventUpdatedAt(eventId)
        }
    }
}
