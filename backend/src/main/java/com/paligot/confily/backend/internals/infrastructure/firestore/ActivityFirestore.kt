package com.paligot.confily.backend.internals.infrastructure.firestore

import com.google.cloud.firestore.Firestore
import com.paligot.confily.backend.internals.helpers.database.getDocument
import com.paligot.confily.backend.internals.helpers.database.getDocuments
import com.paligot.confily.backend.internals.helpers.database.insert
import com.paligot.confily.backend.internals.helpers.database.update

private const val CollectionName = "activities"

class ActivityFirestore(
    private val projectName: String,
    private val firestore: Firestore
) {
    fun get(eventId: String, id: String): ActivityEntity? = firestore
        .collection(projectName)
        .document(eventId)
        .collection(CollectionName)
        .getDocument(id)

    fun getAll(eventId: String): List<ActivityEntity> = firestore
        .collection(projectName)
        .document(eventId)
        .collection(CollectionName)
        .getDocuments<ActivityEntity>()

    fun createOrUpdate(eventId: String, item: ActivityEntity) {
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
