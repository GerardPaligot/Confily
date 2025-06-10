package com.paligot.confily.backend.internals.infrastructure.storage

import com.paligot.confily.backend.internals.helpers.storage.MimeType
import com.paligot.confily.backend.internals.helpers.storage.Storage
import com.paligot.confily.backend.internals.helpers.storage.Upload
import com.paligot.confily.models.AgendaV4
import com.paligot.confily.models.ExportEvent
import com.paligot.confily.models.PartnersActivities
import kotlinx.serialization.json.Json

class EventStorage(
    private val storage: Storage
) {
    suspend fun getEventFile(eventId: String, updateAt: Long): ExportEvent? =
        storage.download("$eventId/event/exports/$updateAt.json")
            ?.let { Json.decodeFromString(it.decodeToString()) }

    suspend fun uploadEventFile(eventId: String, updateAt: Long, event: ExportEvent): Upload =
        storage.upload(
            filename = "$eventId/event/exports/$updateAt.json",
            content = Json.encodeToString(event).toByteArray(),
            mimeType = MimeType.JSON
        )

    suspend fun getPlanningFile(eventId: String, updateAt: Long): AgendaV4? =
        storage.download("$eventId/planning/exports/$updateAt.json")
            ?.let { Json.decodeFromString(it.decodeToString()) }

    suspend fun uploadPlanningFile(eventId: String, updateAt: Long, planning: AgendaV4): Upload =
        storage.upload(
            filename = "$eventId/planning/exports/$updateAt.json",
            content = Json.encodeToString(planning).toByteArray(),
            mimeType = MimeType.JSON
        )

    suspend fun getPartnersFile(eventId: String, updateAt: Long): PartnersActivities? =
        storage.download("$eventId/partners/exports/$updateAt.json")
            ?.let { Json.decodeFromString(it.decodeToString()) }

    suspend fun uploadPartnersFile(
        eventId: String,
        updateAt: Long,
        partners: PartnersActivities
    ): Upload = storage.upload(
        filename = "$eventId/partners/exports/$updateAt.json",
        content = Json.encodeToString(partners).toByteArray(),
        mimeType = MimeType.JSON
    )
}
