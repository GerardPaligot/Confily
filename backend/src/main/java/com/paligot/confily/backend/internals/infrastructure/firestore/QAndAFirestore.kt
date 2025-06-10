package com.paligot.confily.backend.internals.infrastructure.firestore

import com.google.cloud.firestore.Firestore
import com.paligot.confily.backend.internals.helpers.database.batchDelete
import com.paligot.confily.backend.internals.helpers.database.diffRefs
import com.paligot.confily.backend.internals.helpers.database.getDocument
import com.paligot.confily.backend.internals.helpers.database.getDocuments
import com.paligot.confily.backend.internals.helpers.database.insert
import com.paligot.confily.backend.internals.helpers.database.query
import com.paligot.confily.backend.internals.helpers.database.update
import com.paligot.confily.backend.internals.helpers.database.whereEquals

private const val CollectionName = "qanda"

class QAndAFirestore(
    private val projectName: String,
    private val firestore: Firestore
) {
    fun get(eventId: String, id: String): QAndAEntity? = firestore
        .collection(projectName)
        .document(eventId)
        .collection(CollectionName)
        .getDocument(id)

    fun getAll(eventId: String): List<QAndAEntity> = firestore
        .collection(projectName)
        .document(eventId)
        .collection(CollectionName)
        .getDocuments<QAndAEntity>()

    fun getAll(eventId: String, language: String): List<QAndAEntity> = firestore
        .collection(projectName)
        .document(eventId)
        .collection(CollectionName)
        .query<QAndAEntity>("language".whereEquals(language))

    fun createOrUpdate(eventId: String, item: QAndAEntity) {
        if (item.id == null) {
            firestore
                .collection(projectName)
                .document(eventId)
                .collection(CollectionName)
                .insert { item.copy(id = it) }
        } else {
            firestore
                .collection(projectName)
                .document(eventId)
                .collection(CollectionName)
                .update(item.id, item)
        }
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
