package com.paligot.confily.backend.tags

import com.google.cloud.firestore.Firestore
import com.paligot.confily.backend.internals.helpers.database.batchDelete
import com.paligot.confily.backend.internals.helpers.database.diffRefs
import com.paligot.confily.backend.internals.helpers.database.getDocument
import com.paligot.confily.backend.internals.helpers.database.getDocuments
import com.paligot.confily.backend.internals.helpers.database.insert
import com.paligot.confily.backend.internals.helpers.database.update

private const val CollectionName = "tags"

class TagDao(
    private val projectName: String,
    private val firestore: Firestore
) {
    fun get(eventId: String, id: String): TagDb? = firestore
        .collection(projectName)
        .document(eventId)
        .collection(CollectionName)
        .getDocument(id)

    fun getAll(eventId: String): List<TagDb> = firestore
        .collection(projectName)
        .document(eventId)
        .collection(CollectionName)
        .getDocuments<TagDb>()

    fun createOrUpdate(eventId: String, item: TagDb) {
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
