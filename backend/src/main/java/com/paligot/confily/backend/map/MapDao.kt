package com.paligot.confily.backend.map

import com.google.cloud.firestore.Firestore
import com.paligot.confily.backend.NotFoundException
import com.paligot.confily.backend.internals.helpers.database.getDocument
import com.paligot.confily.backend.internals.helpers.database.getDocuments
import com.paligot.confily.backend.internals.helpers.database.insert
import com.paligot.confily.backend.internals.helpers.database.upsert
import com.paligot.confily.backend.internals.helpers.storage.MimeType
import com.paligot.confily.backend.internals.helpers.storage.Storage
import com.paligot.confily.backend.internals.helpers.storage.Upload
import kotlinx.coroutines.coroutineScope

private const val CollectionName = "maps"

class MapDao(
    private val projectName: String,
    private val firestore: Firestore,
    private val storage: Storage
) {
    fun getAll(eventId: String): List<MapDb> = firestore
        .collection(projectName)
        .document(eventId)
        .collection(CollectionName)
        .getDocuments()

    fun get(eventId: String, id: String): MapDb? = firestore
        .collection(projectName)
        .document(eventId)
        .collection(CollectionName)
        .getDocument(id)

    fun createOrUpdate(eventId: String, map: MapDb): String {
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

    suspend fun uploadMap(
        eventId: String,
        mapId: String,
        content: ByteArray
    ): Upload = storage.upload(
        filename = "$eventId/maps/$mapId",
        content = content,
        mimeType = MimeType.PNG
    )

    suspend fun delete(eventId: String, id: String) = coroutineScope {
        val map = get(eventId, id) ?: throw NotFoundException("Map not found")
        firestore
            .collection(projectName)
            .document(eventId)
            .collection(CollectionName)
            .document(id)
            .delete()
        storage.delete("$eventId/maps/${map.filename}")
    }
}
