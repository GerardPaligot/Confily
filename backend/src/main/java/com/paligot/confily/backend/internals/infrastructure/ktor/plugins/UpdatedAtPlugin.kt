package com.paligot.confily.backend.internals.infrastructure.ktor.plugins

import com.paligot.confily.backend.internals.infrastructure.factory.FirestoreModule.eventFirestore
import io.ktor.http.HttpMethod
import io.ktor.server.application.createRouteScopedPlugin
import io.ktor.server.request.httpMethod

val UpdatedAtPlugin = createRouteScopedPlugin("UpdatedAtPlugin") {
    val eventDao by eventFirestore

    onCallRespond { call ->
        if (call.request.httpMethod == HttpMethod.Post || call.request.httpMethod == HttpMethod.Put) {
            val eventId = call.parameters["eventId"] ?: return@onCallRespond
            eventDao.updateUpdatedAt(eventId)
        }
    }
}
