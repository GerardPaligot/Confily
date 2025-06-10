package com.paligot.confily.backend.internals.infrastructure.firestore

import com.google.cloud.firestore.Firestore
import com.paligot.confily.backend.internals.helpers.database.batchDelete
import com.paligot.confily.backend.internals.helpers.database.diffRefs
import com.paligot.confily.backend.internals.helpers.database.getDocument
import com.paligot.confily.backend.internals.helpers.database.getDocuments
import com.paligot.confily.backend.internals.helpers.database.insert
import com.paligot.confily.backend.internals.helpers.database.query
import com.paligot.confily.backend.internals.helpers.database.upsert
import com.paligot.confily.backend.internals.helpers.database.whereIn

private const val CollectionName = "speakers"

class SpeakerFirestore(
    private val projectName: String,
    private val firestore: Firestore
) {
    fun get(eventId: String, id: String): SpeakerEntity? = firestore
        .collection(projectName)
        .document(eventId)
        .collection(CollectionName)
        .getDocument(id)

    fun getByIds(eventId: String, ids: List<String>): List<SpeakerEntity> =
        try {
            firestore
                .collection(projectName)
                .document(eventId)
                .collection(CollectionName)
                .query<SpeakerEntity>("id".whereIn(ids))
        } catch (ignored: Throwable) {
            emptyList()
        }

    fun getAll(eventId: String): List<SpeakerEntity> = firestore
        .collection(projectName)
        .document(eventId)
        .collection(CollectionName)
        .getDocuments()

    fun insert(eventId: String, speaker: SpeakerEntity) {
        firestore
            .collection(projectName)
            .document(eventId)
            .collection(CollectionName)
            .insert(speaker.id, speaker)
    }

    fun createOrUpdate(eventId: String, speaker: SpeakerEntity): String {
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

    fun deleteDiff(eventId: String, ids: List<String>) {
        val diff = firestore
            .collection(projectName)
            .document(eventId)
            .collection(CollectionName)
            .diffRefs(ids)
        firestore.batchDelete(diff)
    }
}
