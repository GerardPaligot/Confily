package com.paligot.confily.backend.events

import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.SetOptions
import com.paligot.confily.backend.NotFoundException
import com.paligot.confily.backend.internals.helpers.database.getDocument
import com.paligot.confily.backend.internals.helpers.database.getDocuments
import com.paligot.confily.backend.internals.helpers.database.update
import com.paligot.confily.backend.internals.helpers.database.upsert
import com.paligot.confily.backend.internals.helpers.storage.MimeType
import com.paligot.confily.backend.internals.helpers.storage.Storage
import com.paligot.confily.backend.internals.helpers.storage.Upload
import com.paligot.confily.models.AgendaV4
import com.paligot.confily.models.ExportEvent
import com.paligot.confily.models.PartnersActivities
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Suppress("TooManyFunctions")
class EventDao(
    private val projectName: String,
    private val firestore: Firestore,
    private val storage: Storage
) {
    fun list(): List<EventDb> = firestore
        .collection(projectName)
        .getDocuments<EventDb>()
        .filter { it.published }

    fun get(id: String): EventDb = firestore
        .collection(projectName)
        .getDocument(id)
        ?: throw NotFoundException("Event $id Not Found")

    fun createOrUpdate(event: EventDb) {
        firestore
            .collection(projectName)
            .upsert(event.slugId, event.copy(updatedAt = System.currentTimeMillis()))
    }

    fun updateMenus(eventId: String, menus: List<LunchMenuDb>) {
        val existing = get(eventId)
        firestore
            .collection(projectName)
            .update(eventId, existing.copy(menus = menus, updatedAt = System.currentTimeMillis()))
    }

    fun updateCoc(eventId: String, coc: String) {
        val existing = get(eventId)
        firestore
            .collection(projectName)
            .update(eventId, existing.copy(coc = coc, updatedAt = System.currentTimeMillis()))
    }

    fun updateFeatures(eventId: String, hasNetworking: Boolean) {
        val existing = get(eventId)
        firestore
            .collection(projectName)
            .update(
                eventId,
                existing.copy(
                    features = FeaturesActivatedDb(hasNetworking = hasNetworking),
                    updatedAt = System.currentTimeMillis()
                )
            )
    }

    fun updateUpdatedAt(eventId: String) {
        firestore
            .collection(projectName)
            .document(eventId)
            .set(mapOf("updatedAt" to System.currentTimeMillis()), SetOptions.merge())
    }

    fun updateEventUpdatedAt(eventId: String) {
        firestore
            .collection(projectName)
            .document(eventId)
            .set(mapOf("eventUpdatedAt" to System.currentTimeMillis()), SetOptions.merge())
    }

    fun updateAgendaUpdatedAt(eventId: String) {
        firestore
            .collection(projectName)
            .document(eventId)
            .set(mapOf("agendaUpdatedAt" to System.currentTimeMillis()), SetOptions.merge())
    }

    fun updatePartnersUpdatedAt(eventId: String) {
        firestore
            .collection(projectName)
            .document(eventId)
            .set(mapOf("partnersUpdatedAt" to System.currentTimeMillis()), SetOptions.merge())
    }

    fun updateAgendaUpdatedAt(event: EventDb) {
        firestore
            .collection(projectName)
            .update(event.slugId, event.copy(agendaUpdatedAt = System.currentTimeMillis()))
    }

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

    @Deprecated("use planning file instead")
    suspend fun getAgendaFile(eventId: String, updateAt: Long): AgendaV4? =
        storage.download("$eventId/agenda/$updateAt.json")
            ?.let { Json.decodeFromString(it.decodeToString()) }

    @Deprecated("use planning file instead")
    suspend fun uploadAgendaFile(eventId: String, updateAt: Long, agendaV4: AgendaV4): Upload =
        storage.upload(
            filename = "$eventId/agenda/$updateAt.json",
            content = Json.encodeToString(agendaV4).toByteArray(),
            mimeType = MimeType.JSON
        )
}
