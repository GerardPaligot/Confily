package com.paligot.confily.backend.events

import com.google.cloud.firestore.Firestore
import com.paligot.confily.backend.NotAuthorized
import com.paligot.confily.backend.NotFoundException
import com.paligot.confily.backend.internals.helpers.database.getDocument
import com.paligot.confily.backend.internals.helpers.database.getDocuments
import com.paligot.confily.backend.internals.helpers.database.update
import com.paligot.confily.backend.internals.helpers.database.upsert
import com.paligot.confily.backend.internals.helpers.storage.MimeType
import com.paligot.confily.backend.internals.helpers.storage.Storage
import com.paligot.confily.backend.internals.helpers.storage.Upload
import com.paligot.confily.models.AgendaV4
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class EventDao(
    private val projectName: String,
    private val firestore: Firestore,
    private val storage: Storage
) {
    fun list(): List<EventDb> = firestore
        .collection(projectName)
        .getDocuments<EventDb>()
        .filter { it.published }

    fun get(id: String): EventDb? = firestore
        .collection(projectName)
        .getDocument(id)

    fun getVerified(id: String, apiKey: String?): EventDb {
        val eventDb = firestore
            .collection(projectName)
            .getDocument<EventDb>(id)
            ?: throw NotFoundException("Event $id Not Found")
        return if (eventDb.apiKey == apiKey) eventDb else throw NotAuthorized
    }

    fun createOrUpdate(event: EventDb) {
        firestore
            .collection(projectName)
            .upsert(event.slugId, event.copy(updatedAt = System.currentTimeMillis()))
    }

    fun updateMenus(eventId: String, apiKey: String, menus: List<LunchMenuDb>) {
        val existing = getVerified(eventId, apiKey)
        firestore
            .collection(projectName)
            .update(eventId, existing.copy(menus = menus, updatedAt = System.currentTimeMillis()))
    }

    fun updateCoc(eventId: String, apiKey: String, coc: String) {
        val existing = getVerified(eventId, apiKey)
        firestore
            .collection(projectName)
            .update(eventId, existing.copy(coc = coc, updatedAt = System.currentTimeMillis()))
    }

    fun updateFeatures(eventId: String, apiKey: String, hasNetworking: Boolean) {
        val existing = getVerified(eventId, apiKey)
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

    fun updateUpdatedAt(event: EventDb) {
        firestore
            .collection(projectName)
            .update(event.slugId, event.copy(updatedAt = System.currentTimeMillis()))
    }

    fun updateAgendaUpdatedAt(event: EventDb) {
        firestore
            .collection(projectName)
            .update(event.slugId, event.copy(agendaUpdatedAt = System.currentTimeMillis()))
    }

    suspend fun getAgendaFile(eventId: String, updateAt: Long): AgendaV4? =
        storage.download("$eventId/agenda/$updateAt.json")
            ?.let { Json.decodeFromString(it.decodeToString()) }

    suspend fun uploadAgendaFile(eventId: String, updateAt: Long, agendaV4: AgendaV4): Upload =
        storage.upload(
            filename = "$eventId/agenda/$updateAt.json",
            content = Json.encodeToString(agendaV4).toByteArray(),
            mimeType = MimeType.JSON
        )
}
