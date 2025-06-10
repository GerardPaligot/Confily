package com.paligot.confily.backend.internals.infrastructure.firestore

import com.google.cloud.firestore.Firestore
import com.paligot.confily.backend.NotFoundException
import com.paligot.confily.backend.internals.helpers.database.getDocument
import com.paligot.confily.backend.internals.helpers.database.getDocuments
import com.paligot.confily.backend.internals.helpers.database.insert
import com.paligot.confily.backend.internals.helpers.database.upsert
import com.paligot.confily.backend.internals.helpers.storage.Storage

private const val CollectionName = "maps"

class MapFirestore(
    private val projectName: String,
    private val firestore: Firestore,
    private val storage: Storage
) {
    fun getAll(eventId: String): List<MapEntity> = firestore
        .collection(projectName)
        .document(eventId)
        .collection(CollectionName)
        .getDocuments()

    fun get(eventId: String, id: String): MapEntity? = firestore
        .collection(projectName)
        .document(eventId)
        .collection(CollectionName)
        .getDocument(id)

    fun createOrUpdate(eventId: String, map: MapEntity): String {
        if (map.id == "") {
            return firestore
                .collection(projectName)
                .document(eventId)
                .collection(CollectionName)
                .insert { map.copy(id = it) }
        }
        firestore
            .collection(projectName)
            .document(eventId)
            .collection(CollectionName)
            .upsert(map.id, map)
        return map.id
    }

    suspend fun delete(eventId: String, id: String): String {
        val map = get(eventId, id) ?: throw NotFoundException("Map not found")
        firestore
            .collection(projectName)
            .document(eventId)
            .collection(CollectionName)
            .document(id)
            .delete()
        return map.filename
    }
}
