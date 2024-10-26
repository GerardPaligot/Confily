package com.paligot.confily.backend.activities

import com.google.cloud.firestore.Firestore
import com.paligot.confily.backend.internals.helpers.database.getDocument
import com.paligot.confily.backend.internals.helpers.database.getDocuments
import com.paligot.confily.backend.internals.helpers.database.insert
import com.paligot.confily.backend.internals.helpers.database.update

private const val CollectionName = "activities"

class ActivityDao(
    private val projectName: String,
    private val firestore: Firestore
) {
    fun get(eventId: String, id: String): ActivityDb? = firestore
        .collection(projectName)
        .document(eventId)
        .collection(CollectionName)
        .getDocument(id)

    fun getAll(eventId: String): List<ActivityDb> = firestore
        .collection(projectName)
        .document(eventId)
        .collection(CollectionName)
        .getDocuments<ActivityDb>()

    fun createOrUpdate(eventId: String, item: ActivityDb) {
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
}
