package com.paligot.confily.backend.speakers

import com.google.cloud.firestore.Firestore
import com.paligot.confily.backend.internals.helpers.database.batchDelete
import com.paligot.confily.backend.internals.helpers.database.diffRefs
import com.paligot.confily.backend.internals.helpers.database.getDocument
import com.paligot.confily.backend.internals.helpers.database.getDocuments
import com.paligot.confily.backend.internals.helpers.database.insert
import com.paligot.confily.backend.internals.helpers.database.query
import com.paligot.confily.backend.internals.helpers.database.upsert
import com.paligot.confily.backend.internals.helpers.database.whereIn
import com.paligot.confily.backend.internals.helpers.storage.MimeType
import com.paligot.confily.backend.internals.helpers.storage.Storage
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

private const val CollectionName = "speakers"

class SpeakerDao(
    private val projectName: String,
    private val firestore: Firestore,
    private val storage: Storage
) {
    fun get(eventId: String, id: String): SpeakerDb? = firestore
        .collection(projectName)
        .document(eventId)
        .collection(CollectionName)
        .getDocument(id)

    fun getByIds(eventId: String, ids: List<String>): List<SpeakerDb> =
        try {
            firestore
                .collection(projectName)
                .document(eventId)
                .collection(CollectionName)
                .query<SpeakerDb>("id".whereIn(ids))
        } catch (ignored: Throwable) {
            emptyList()
        }

    fun getAll(eventId: String): List<SpeakerDb> = firestore
        .collection(projectName)
        .document(eventId)
        .collection(CollectionName)
        .getDocuments()

    fun insert(eventId: String, speaker: SpeakerDb) {
        firestore
            .collection(projectName)
            .document(eventId)
            .collection(CollectionName)
            .insert(speaker.id, speaker)
    }

    suspend fun insertAll(eventId: String, speakers: List<SpeakerDb>) = coroutineScope {
        val asyncItems = speakers.map {
            async {
                firestore
                    .collection(projectName)
                    .document(eventId)
                    .collection(CollectionName)
                    .insert(it.id, it)
            }
        }
        asyncItems.awaitAll()
        Unit
    }

    fun createOrUpdate(eventId: String, speaker: SpeakerDb): String {
        if (speaker.id == "") {
            return firestore
                .collection(projectName)
                .document(eventId)
                .collection(CollectionName)
                .insert { speaker.copy(id = it) }
        }
        firestore
            .collection(projectName)
            .document(eventId)
            .collection(CollectionName)
            .upsert(speaker.id, speaker)
        return speaker.id
    }

    suspend fun saveProfile(eventId: String, id: String, content: ByteArray, mimeType: MimeType) =
        storage.upload(
            filename = "$eventId/speakers/$id.png",
            content = content,
            mimeType = mimeType
        )

    fun deleteDiff(eventId: String, ids: List<String>) {
        val diff = firestore
            .collection(projectName)
            .document(eventId)
            .collection(CollectionName)
            .diffRefs(ids)
        firestore.batchDelete(diff)
    }
}
