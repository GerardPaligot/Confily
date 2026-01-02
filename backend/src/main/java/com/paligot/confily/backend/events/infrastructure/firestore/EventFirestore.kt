package com.paligot.confily.backend.events.infrastructure.firestore

import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.SetOptions
import com.paligot.confily.backend.NotFoundException
import com.paligot.confily.backend.internals.helpers.database.getDocument
import com.paligot.confily.backend.internals.helpers.database.getDocuments
import com.paligot.confily.backend.internals.helpers.database.update
import com.paligot.confily.backend.internals.helpers.database.upsert
import com.paligot.confily.backend.menus.infrastructure.firestore.LunchMenuEntity

@Suppress("TooManyFunctions")
class EventFirestore(
    private val projectName: String,
    private val firestore: Firestore
) {
    fun list(): List<EventEntity> = firestore
        .collection(projectName)
        .getDocuments<EventEntity>()
        .filter { it.published }

    fun get(id: String): EventEntity = firestore
        .collection(projectName)
        .getDocument(id)
        ?: throw NotFoundException("Event $id Not Found")

    fun createOrUpdate(event: EventEntity) {
        firestore
            .collection(projectName)
            .upsert(event.slugId, event.copy(updatedAt = System.currentTimeMillis()))
    }

    fun updateMenus(eventId: String, menus: List<LunchMenuEntity>) {
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
                    features = FeaturesActivatedEntity(hasNetworking = hasNetworking),
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

    fun updateAgendaUpdatedAt(event: EventEntity) {
        firestore
            .collection(projectName)
            .update(event.slugId, event.copy(agendaUpdatedAt = System.currentTimeMillis()))
    }
}
